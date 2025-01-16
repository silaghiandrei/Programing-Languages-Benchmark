package tests;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ContextSwitch {
    private static final AtomicInteger readyFlag = new AtomicInteger(0);

    static class ThreadParams {
        int threadId;
        long[] timings;

        public ThreadParams(int threadId, long[] timings) {
            this.threadId = threadId;
            this.timings = timings;
        }
    }

    static class TestThread extends Thread {
        private final ThreadParams params;

        public TestThread(ThreadParams params) {
            this.params = params;
        }

        @Override
        public void run() {
            int id = params.threadId;

            try {
                java.lang.ProcessHandle.current().info();
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (readyFlag.get() != id) {
                Thread.onSpinWait();
            }

            params.timings[id] = System.nanoTime();

            readyFlag.incrementAndGet();
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java ContextSwitchTest <number_of_tests> <number_of_threads>");
            return;
        }

        int numTests = Integer.parseInt(args[0]);
        int numThreads = Integer.parseInt(args[1]);

        String outputPath = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\context_switch_results.txt";
        try (FileWriter writer = new FileWriter(outputPath, true)) {
            writer.write("Java\n");

            double totalTotalTime = 0.0;

            for (int test = 0; test < numTests + 10; ++test) {
                readyFlag.set(0);
                Thread[] threads = new Thread[numThreads];
                long[] timings = new long[numThreads];

                for (int i = 0; i < numThreads; ++i) {
                    threads[i] = new TestThread(new ThreadParams(i, timings));
                }

                long startTime = System.nanoTime();
                timings[0] = startTime;

                for (Thread thread : threads) {
                    thread.start();
                }

                for (Thread thread : threads) {
                    thread.join();
                }

                double totalTime = 0.0;

                for (int i = 1; i < numThreads; ++i) {
                    double duration = (timings[i] - timings[i - 1]) / 1_000.0;
                    totalTime += duration;
                }

                if (test >= 10) {
                    totalTotalTime += totalTime / (numThreads - 1);
                    writer.write(String.format("%d %.3f%n", test + 1 - 10, totalTime / (numThreads - 1)));
                }
            }

            writer.write(String.format("Average %.3f%n", totalTotalTime / numTests));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Results written to " + outputPath);
    }
}