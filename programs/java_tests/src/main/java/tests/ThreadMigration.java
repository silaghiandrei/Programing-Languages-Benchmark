package tests;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadMigration {
    public interface Kernel32 extends StdCallLibrary {
        Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

        Pointer GetCurrentThread();

        long SetThreadAffinityMask(Pointer hThread, long dwThreadAffinityMask);
    }

    static class ThreadData {
        public int threadId;
        public int coreToStart;
        public int coreToMigrate;

        public ThreadData(int threadId, int coreToStart, int coreToMigrate) {
            this.threadId = threadId;
            this.coreToStart = coreToStart;
            this.coreToMigrate = coreToMigrate;
        }
    }

    private static final AtomicInteger migrationCount = new AtomicInteger(0);
    private static final AtomicLong totalMigrationTime = new AtomicLong(0);

    private static void setThreadAffinity(int core) {
        long affinityMask = 1L << core;
        Kernel32.INSTANCE.SetThreadAffinityMask(Kernel32.INSTANCE.GetCurrentThread(), affinityMask);
    }

    static class MigrationTask implements Runnable {
        private final ThreadData data;

        public MigrationTask(ThreadData data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                setThreadAffinity(data.coreToStart);

                long startTime = System.nanoTime();

                setThreadAffinity(data.coreToMigrate);

                long endTime = System.nanoTime();

                long durationMicroseconds = (endTime - startTime) / 1_000;

                totalMigrationTime.addAndGet(durationMicroseconds);
                migrationCount.incrementAndGet();

            } catch (Exception e) {
                System.err.println("Error in thread " + data.threadId + ": " + e.getMessage());
            }
        }
    }

    private static void runTest(int testIndex, int numThreads, BufferedWriter outputFile,
                                AtomicInteger totalMigrations, AtomicLong overallTotalDuration) throws IOException {
        Thread[] threads = new Thread[numThreads];
        ThreadData[] threadData = new ThreadData[numThreads];

        migrationCount.set(0);
        totalMigrationTime.set(0);

        for (int i = 0; i < numThreads; i++) {
            threadData[i] = new ThreadData(i, i % 4, (i + 1) % 4);
            threads[i] = new Thread(new MigrationTask(threadData[i]));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Thread interrupted: " + e.getMessage());
            }
        }

        double testTotalDuration = totalMigrationTime.get();
        double testAverageDuration = testTotalDuration / migrationCount.get();

        outputFile.write((testIndex + 1) + " " + String.format("%.6f", testAverageDuration) + "\n");

        overallTotalDuration.addAndGet((long) testAverageDuration);
        totalMigrations.addAndGet(migrationCount.get());
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Not enough arguments");
            System.exit(1);
        }

        int numTests = Integer.parseInt(args[0]);
        int numThreads = Integer.parseInt(args[1]);

        if (numTests <= 0 || numThreads <= 0) {
            System.err.println("Both arguments must be positive integers.");
            System.exit(1);
        }

        String outputPath = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\thread_migration_results.txt";

        try (BufferedWriter outputFile = new BufferedWriter(new FileWriter(outputPath, true))) {
            outputFile.write("Java\n");

            AtomicInteger totalMigrations = new AtomicInteger(0);
            AtomicLong overallTotalDuration = new AtomicLong(0);

            for (int i = 0; i < numTests; i++) {
                runTest(i, numThreads, outputFile, totalMigrations, overallTotalDuration);
            }

            double overallAverageDuration = overallTotalDuration.get() / (double) numTests;

            outputFile.write("Average " + String.format("%.3f", overallAverageDuration) + "\n");

            System.out.println("Test complete. Results written to file: " + outputPath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
