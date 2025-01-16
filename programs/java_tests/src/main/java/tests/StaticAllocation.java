package tests;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class StaticAllocation {
    static final int SIZE = 1000;

    static double getTime() {
        return System.nanoTime() / 1e3;
    }

    static void useFunction(int[] arr) {
        for (int i = 0; i < SIZE; i++) {
            arr[i] = i;
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Not enough arguments");
            return;
        }

        int testsNo;
        try {
            testsNo = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid argument");
            return;
        }

        String filePath = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\static_allocation_results.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.println("Java");

            double sum = 0.0;
            for (int i = 0; i < testsNo + 5; i++) {
                if (i >= 5) {
                    double startTime = getTime();
                    final int[] array = new int[SIZE];
                    double endTime = getTime();
                    double duration = endTime - startTime;
                    sum += duration;

                    useFunction(array);

                    writer.printf("%d %.6f%n", i + 1 - 5, duration);
                }
            }
            sum /= testsNo;
            writer.printf("Average %.3f%n", sum);

            System.out.println("Results appended to " + filePath);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
