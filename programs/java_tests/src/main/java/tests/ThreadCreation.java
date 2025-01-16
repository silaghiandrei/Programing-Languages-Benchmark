package tests;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ThreadCreation {

    private static void threadFunction() {
    }

    private static double measureThreadCreationTime(int numThreads) {
        Thread[] threads = new Thread[numThreads];
        double totalCreationTime = 0.0;

        for (int i = 0; i < numThreads; i++) {
            long startTime = System.nanoTime();
            threads[i] = new Thread(ThreadCreation::threadFunction);
            threads[i].start();
            long endTime = System.nanoTime();

            totalCreationTime += (endTime - startTime) / 1_000.0;
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                if (threads[i] != null) {
                    threads[i].join();
                }
            } catch (InterruptedException e) {
                System.err.println("Thread interrupted: " + e.getMessage());
            }
        }

        return totalCreationTime;
    }

    private static double runTest(BufferedWriter file, int testIndex, int numThreads) throws IOException {
        double duration = measureThreadCreationTime(numThreads);
        file.write(String.format("%d %.6f%n", testIndex + 1, duration));
        return duration;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Not enough arguments");
            return;
        }

        int numTests, numThreads;
        try {
            numTests = Integer.parseInt(args[0]);
            numThreads = Integer.parseInt(args[1]);

            if (numTests <= 0 || numThreads <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println("Both arguments must be positive integers.");
            return;
        }

        String filePath = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\thread_creation_results.txt";

        try (BufferedWriter file = new BufferedWriter(new FileWriter(filePath, true))) {
            file.write("Java\n");
            double totalTime = 0.0;

            for (int i = 0; i < numTests; i++) {
                totalTime += runTest(file, i, numThreads);
            }

            double averageTime = totalTime / numTests;
            file.write(String.format("Average %.3f%n", averageTime));
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
