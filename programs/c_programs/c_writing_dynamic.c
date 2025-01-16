#include <stdio.h>
#include <stdlib.h>
#include <windows.h>

double get_time() {
    LARGE_INTEGER frequency;
    LARGE_INTEGER time;

    QueryPerformanceFrequency(&frequency);
    QueryPerformanceCounter(&time);

    return (double)time.QuadPart / frequency.QuadPart;
}

int main(int argc, char* argv[]) {
    if (argc < 3) {
        printf("Not enough arguments");
        return -1;
    }

    FILE *file = fopen("E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\dynamic_writing_results.txt", "w");

    if (file == NULL) {
        printf("Error opening the file\n");
        return -1;
    }

    double start_time = 0.0f;
    double end_time = 0.0f;
    double duration = 0.0f;
    double sum = 0.0f;

    int tests_no, array_size;
    sscanf(argv[1], "%d", &tests_no);
    sscanf(argv[2], "%d", &array_size);

    fprintf(file, "C\n");
    for (int i = 0; i < tests_no + 10; i++) {
        duration = 0.0f;

        int* array = (int*)malloc(array_size * sizeof(int));
        if (array == NULL) {
            fprintf(stderr, "Memory allocation failed\n");
            fclose(file);
            return -1;
        }

        start_time = get_time();
        for (int j = 0; j < array_size; j++) {
            array[j] = j;
        }
        end_time = get_time();
        duration = (end_time - start_time) * 1000000;
        if (i >= 10) {
            sum += duration;
            fprintf(file, "%d %f\n", i + 1 - 10, duration);
        }
        free(array);
    }

    sum /= tests_no;

    fprintf(file, "Average %.3f\n", sum);
    
    fclose(file);
    return 0;
}
