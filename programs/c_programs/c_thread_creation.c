#include <windows.h>
#include <stdio.h>
#include <stdlib.h>

DWORD WINAPI thread_function(LPVOID arg) {
    return 0;
}

double measure_thread_creation_time(int num_threads) {
    HANDLE* threads = malloc(num_threads * sizeof(HANDLE));
    if (threads == NULL) {
        perror("malloc");
        exit(EXIT_FAILURE);
    }

    LARGE_INTEGER frequency, start, end;
    QueryPerformanceFrequency(&frequency);

    double total_creation_time = 0.0;

    for (int i = 0; i < num_threads; i++) {
        QueryPerformanceCounter(&start);
        threads[i] = CreateThread(NULL, 0, thread_function, NULL, 0, NULL);
        QueryPerformanceCounter(&end);

        if (threads[i] == NULL) {
            fprintf(stderr, "Error creating thread %d\n", i);
            free(threads);
            exit(EXIT_FAILURE);
        }

        total_creation_time += (double)(end.QuadPart - start.QuadPart) / frequency.QuadPart * 1e6;
    }

    for (int i = 0; i < num_threads; i++) {
        WaitForSingleObject(threads[i], INFINITE);
        CloseHandle(threads[i]);
    }

    free(threads);
    return total_creation_time;
}

int main(int argc, char* argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Not enough arguments");
        return EXIT_FAILURE;
    }

    int num_threads, num_tests;
    if (sscanf(argv[1], "%d", &num_tests) != 1 || sscanf(argv[2], "%d", &num_threads) != 1) {
        fprintf(stderr, "Both arguments must be integers.\n");
        return EXIT_FAILURE;
    }

    FILE* file = fopen("E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\thread_creation_results.txt", "w");
    if (file == NULL) {
        perror("fopen");
        return EXIT_FAILURE;
    }

    fprintf(file, "C\n");

    double total_time = 0.0;

    for (int i = 0; i < num_tests; i++) {
        double creation_time = measure_thread_creation_time(num_threads);
        fprintf(file, "%d %f\n", i + 1, creation_time);
        total_time += creation_time;
    }

    double average_time = total_time / num_tests;
    fprintf(file, "Average %.3f\n", average_time);

    fclose(file);

    return EXIT_SUCCESS;
}
