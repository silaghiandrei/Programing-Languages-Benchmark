package tests;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DynamicAllocation {
    static double getTime() {
        return System.nanoTime() / 1e3;
    }

    static void useFunction(Integer[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Not enough arguments");
            return;
        }

        int testsNo;
        int arrayLength;

        try {
            testsNo = Integer.parseInt(args[0]);
            arrayLength = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid argument(s)");
            return;
        }

        String filePath = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\dynamic_allocation_results.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.println("Java");

            double totalDuration = 0;

            for (int i = 0; i < testsNo + 5; i++) {
                double startTime = getTime();

                Integer[] array = new Integer[arrayLength];

                double endTime = getTime();
                double duration = endTime - startTime;

                useFunction(array);
                if (i >= 5) {
                    totalDuration += duration;
                    writer.printf("%d %.6f%n", i + 1 - 5, duration);
                }
            }

            double averageDuration = totalDuration / testsNo;
            writer.printf("Average %.3f%n", averageDuration);

            System.out.println("Results appended to " + filePath);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
