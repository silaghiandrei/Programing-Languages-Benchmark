package tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DynamicReading {

    private static Integer[] allocateAndFillArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        return array;
    }

    private static double measureLoopExecution(Integer[] array) {
        long startTime = System.nanoTime();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 0) {
            }
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000.0;
    }

    private static double runTest(BufferedWriter file, int testIndex, int size) throws IOException {
        Integer[] array = allocateAndFillArray(size);
        double duration = measureLoopExecution(array);
        if (testIndex >= 10) {
            file.write((testIndex + 1 - 10) + " " + String.format("%.6f", duration));
            file.newLine();
            return duration;
        } else {
            return 0.0;
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Not enough arguments");
            return;
        }

        String filePath = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\dynamic_reading_results.txt";

        try (BufferedWriter file = new BufferedWriter(new FileWriter(filePath, true))) {
            int testsNo = Integer.parseInt(args[0]);
            int arraySize = Integer.parseInt(args[1]);
            double sum = 0.0;

            file.write("Java");
            file.newLine();

            for (int i = 0; i < testsNo + 10; i++) {
                sum += runTest(file, i, arraySize);
            }

            double average = sum / testsNo;
            file.write("Average " + String.format("%.3f", average));
            file.newLine();

        } catch (IOException e) {
            System.out.println("Error opening the file: " + e.getMessage());
        }
    }
}
