#include <windows.h>
#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int thread_id;
    LARGE_INTEGER *timing;
    HANDLE start_event;
    HANDLE ready_event;
} ThreadParams;

DWORD WINAPI ThreadFunction(LPVOID param) {
    ThreadParams *params = (ThreadParams *)param;
    int id = params->thread_id;
    HANDLE start_event = params->start_event;
    HANDLE ready_event = params->ready_event;

    WaitForSingleObject(start_event, INFINITE);

    QueryPerformanceCounter(&params->timing[id]);

    Sleep(0);

    QueryPerformanceCounter(&params->timing[id + 1]);

    SetEvent(ready_event);

    return 0;
}

int main(int argc, char *argv[]) {
    if (argc < 3) {
        printf("Not enough arguments");
        return -1;
    }

    int num_tests = atoi(argv[1]);
    int num_threads = atoi(argv[2]);

    FILE *output = fopen("E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\context_switch_results.txt", "w");
    if (!output) {
        perror("Failed to open output file");
        return -1;
    }

    LARGE_INTEGER frequency;
    QueryPerformanceFrequency(&frequency);

    fprintf(output, "C\n");

    HANDLE *threads = (HANDLE *)malloc(num_threads * sizeof(HANDLE));
    LARGE_INTEGER *timing = (LARGE_INTEGER *)malloc((num_threads + 1) * sizeof(LARGE_INTEGER));
    ThreadParams *params = (ThreadParams *)malloc(num_threads * sizeof(ThreadParams));
    HANDLE *ready_events = (HANDLE *)malloc(num_threads * sizeof(HANDLE));

    double total_total_time = 0.0;

    for (int test = 0; test < num_tests + 10; ++test) {
        double total_time = 0.0;

        HANDLE start_event = CreateEvent(NULL, TRUE, FALSE, NULL);
        if (!start_event) {
            perror("Failed to create start event");
            return -1;
        }

        for (int i = 0; i < num_threads; ++i) {
            ready_events[i] = CreateEvent(NULL, FALSE, FALSE, NULL);
            if (!ready_events[i]) {
                perror("Failed to create ready event");
                return -1;
            }

            params[i].thread_id = i;
            params[i].timing = timing;
            params[i].start_event = start_event;
            params[i].ready_event = ready_events[i];

            threads[i] = CreateThread(
                NULL, 0, ThreadFunction, &params[i], 0, NULL
            );

            if (!threads[i]) {
                perror("Failed to create thread");
                return -1;
            }

            SetThreadAffinityMask(threads[i], 1);
        }

        SetEvent(start_event);

        WaitForMultipleObjects(num_threads, ready_events, TRUE, INFINITE);

        for (int i = 0; i < num_threads; ++i) {
            double duration = (double)(timing[i + 1].QuadPart - timing[i].QuadPart) * 1000000 / frequency.QuadPart;
            total_time += duration;
        }

        if (test >= 10) { 
            total_total_time += total_time / num_threads;
            fprintf(output, "%d %.3f\n", test + 1 - 10, total_time / num_threads);
        }

        for (int i = 0; i < num_threads; ++i) {
            CloseHandle(threads[i]);
            CloseHandle(ready_events[i]);
        }
        CloseHandle(start_event);
    }

    fprintf(output, "Average %.3f\n", total_total_time / num_tests);

    free(threads);
    free(timing);
    free(params);
    free(ready_events);

    fclose(output);
    printf("Results written to context_switch_results.txt\n");
    return 0;
}
