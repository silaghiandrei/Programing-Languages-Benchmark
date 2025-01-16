#include <windows.h>
#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int thread_id;
    DWORD core_to_start;
    DWORD core_to_migrate;
    int *migration_count;
    LARGE_INTEGER *total_migration_time;
    LARGE_INTEGER frequency;
} thread_data_t;

double get_time() {
    LARGE_INTEGER frequency;
    LARGE_INTEGER time;

    QueryPerformanceFrequency(&frequency);
    QueryPerformanceCounter(&time);

    return (double)time.QuadPart / frequency.QuadPart;
}

DWORD WINAPI thread_function(LPVOID arg) {
    thread_data_t *data = (thread_data_t *)arg;
    LARGE_INTEGER start_time, end_time;

    SetThreadAffinityMask(GetCurrentThread(), 1ULL << data->core_to_start);

    start_time.QuadPart = get_time() * 1000000;

    SetThreadAffinityMask(GetCurrentThread(), 1ULL << data->core_to_migrate);

    end_time.QuadPart = get_time() * 1000000;

    LONG duration = (LONG)(end_time.QuadPart - start_time.QuadPart);
    InterlockedAdd((LONG *)&data->total_migration_time->QuadPart, duration);
    InterlockedIncrement((LONG *)data->migration_count);

    return 0;
}

void run_test(int test_index, int num_threads, int *total_migrations, double *overall_total_duration, FILE *output_file) {
    HANDLE *threads = malloc(num_threads * sizeof(HANDLE));
    thread_data_t *thread_data = malloc(num_threads * sizeof(thread_data_t));
    int migration_count = 0;
    LARGE_INTEGER total_migration_time = {0}, frequency;

    QueryPerformanceFrequency(&frequency);

    if (!threads || !thread_data) {
        perror("malloc");
        free(threads);
        free(thread_data);
        exit(EXIT_FAILURE);
    }

    for (int i = 0; i < num_threads; i++) {
        thread_data[i].thread_id = i;
        thread_data[i].core_to_start = i % 4;
        thread_data[i].core_to_migrate = (i + 1) % 4;
        thread_data[i].migration_count = &migration_count;
        thread_data[i].total_migration_time = &total_migration_time;
        thread_data[i].frequency = frequency;

        threads[i] = CreateThread(
            NULL,
            0,
            thread_function,
            &thread_data[i],
            0,
            NULL
        );

        if (threads[i] == NULL) {
            fprintf(stderr, "Error creating thread %d\n", i);
            free(threads);
            free(thread_data);
            exit(EXIT_FAILURE);
        }
    }

    for (int i = 0; i < num_threads; i++) {
        WaitForSingleObject(threads[i], INFINITE);
        CloseHandle(threads[i]);
    }

    double test_total_duration = (double)total_migration_time.QuadPart / frequency.QuadPart * 1e6;
    double test_average_duration = test_total_duration / migration_count;

    fprintf(output_file, "%d %.6f\n", test_index + 1, test_average_duration);

    *overall_total_duration += test_average_duration;
    *total_migrations += migration_count;

    free(threads);
    free(thread_data);
}

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Not enough arguments");
        return EXIT_FAILURE;
    }

    int num_tests = atoi(argv[1]);
    int num_threads = atoi(argv[2]);
    if (num_tests <= 0 || num_threads <= 0) {
        fprintf(stderr, "Both arguments must be positive integers.\n");
        return EXIT_FAILURE;
    }

    FILE *output_file = fopen("E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\thread_migration_results.txt", "w");
    if (output_file == NULL) {
        perror("fopen");
        return EXIT_FAILURE;
    }

    fprintf(output_file, "C\n");

    int total_migrations = 0;
    double total_duration = 0.0;

    for (int i = 0; i < num_tests; i++) {
        run_test(i, num_threads, &total_migrations, &total_duration, output_file);
    }

    double overall_average_duration = total_duration / num_tests;
    fprintf(output_file, "Average %.3f\n", overall_average_duration);

    fclose(output_file);
    return EXIT_SUCCESS;
}
