package tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class StaticWriting {
    private static final int SIZE = 1000;

    static final int[] staticArray = new int[SIZE];

    private static double measureLoopExecution() {
        long startTime = System.nanoTime();
        for (int i = 0; i < staticArray.length; i++) {
            staticArray[i] = i;
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000.0;
    }

    private static double runTest(BufferedWriter file, int testIndex) throws IOException {
        double duration = measureLoopExecution();
        if (testIndex >= 10) {
            file.write((testIndex + 1 - 10) + " " + String.format("%.6f", duration));
            file.newLine();
            return duration;
        } else {
            return 0.0;
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Not enough arguments");
            return;
        }

        String filePath = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\static_writing_results.txt";

        try (BufferedWriter file = new BufferedWriter(new FileWriter(filePath, true))) {
            int testsNo = Integer.parseInt(args[0]);
            double sum = 0.0;

            file.write("Java");
            file.newLine();

            for (int i = 0; i < testsNo + 10; i++) {
                sum += runTest(file, i);
            }

            double average = sum / testsNo;
            file.write("Average " + String.format("%.3f", average));
            file.newLine();

        } catch (IOException e) {
            System.out.println("Error opening the file: " + e.getMessage());
        }
    }
}
