using System;
using System.Diagnostics;
using System.IO;
using System.Runtime.InteropServices;
using System.Threading;

class Program
{
    private class ThreadParams
    {
        public int ThreadId;
        public long[] Timings;
        public int TotalThreads;
    }

    private static volatile int readyFlag = 0;

    [DllImport("kernel32.dll")]
    private static extern IntPtr GetCurrentThread();

    [DllImport("kernel32.dll")]
    private static extern ulong SetThreadAffinityMask(IntPtr hThread, ulong dwThreadAffinityMask);

    static void ThreadFunction(object param)
    {
        ThreadParams parameters = (ThreadParams)param;
        int id = parameters.ThreadId;

        SetThreadAffinityMask(GetCurrentThread(), 1);

        while (Interlocked.CompareExchange(ref readyFlag, id, id) != id)
        {
            Thread.SpinWait(0);
        }

        parameters.Timings[id] = Stopwatch.GetTimestamp();

        Interlocked.Exchange(ref readyFlag, id + 1);
    }

    static void Main(string[] args)
    {
        if (args.Length < 2)
        {
            Console.WriteLine("Not enough arguments");
            return;
        }

        int numTests = int.Parse(args[0]);
        int numThreads = int.Parse(args[1]);

        string outputPath = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\context_switch_results.txt";
        using (StreamWriter output = new StreamWriter(outputPath, true))
        {
            output.WriteLine("C#");

            long frequency = Stopwatch.Frequency;
            double totalTotalTime = 0.0;

            for (int test = 0; test < numTests + 10; ++test)
            {
                readyFlag = 0;

                Thread[] threads = new Thread[numThreads];
                long[] timings = new long[numThreads];
                ThreadParams[] threadParams = new ThreadParams[numThreads];

                for (int i = 0; i < numThreads; ++i)
                {
                    threadParams[i] = new ThreadParams
                    {
                        ThreadId = i,
                        Timings = timings,
                        TotalThreads = numThreads
                    };

                    threads[i] = new Thread(ThreadFunction);
                }

                timings[0] = Stopwatch.GetTimestamp();

                foreach (var thread in threads)
                {
                    thread.Start(threadParams[Array.IndexOf(threads, thread)]);
                }

                foreach (var thread in threads)
                {
                    thread.Join();
                }

                double totalTime = 0.0;

                for (int i = 1; i < numThreads; ++i)
                {
                    double duration = (double)(timings[i] - timings[i - 1]) * 1_000_000 / frequency;
                    totalTime += duration;
                }

                if (test >= 10)
                {
                    totalTotalTime += totalTime / (numThreads - 1);
                    output.WriteLine("{0} {1:F3}", test + 1 - 10, totalTime / (numThreads - 1));
                }
            }

            output.WriteLine("Average {0:F3}", totalTotalTime / numTests);
        }

        Console.WriteLine("Results written to {0}", outputPath);
    }
}