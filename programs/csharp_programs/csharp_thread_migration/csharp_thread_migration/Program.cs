using System;
using System.Diagnostics;
using System.IO;
using System.Threading;

class ThreadMigration
{
    struct ThreadData
    {
        public int ThreadId;
        public int CoreToStart;
        public int CoreToMigrate;
    }

    static object lockObj = new object();
    static int migrationCount = 0;
    static long totalMigrationTime = 0;

    static void ThreadFunction(object obj)
    {
        var data = (ThreadData)obj;

        SetThreadAffinity(data.CoreToStart);

        long startTime = Stopwatch.GetTimestamp();

        SetThreadAffinity(data.CoreToMigrate);

        long endTime = Stopwatch.GetTimestamp();

        long elapsedTicks = endTime - startTime;

        lock (lockObj)
        {
            migrationCount++;
            totalMigrationTime += elapsedTicks;
        }
    }

    [System.Runtime.InteropServices.DllImport("kernel32.dll")]
    private static extern IntPtr GetCurrentThread();

    static void SetThreadAffinity(int core)
    {
        var mask = (UIntPtr)(1 << core);
        IntPtr currentThreadHandle = GetCurrentThread();
        WinAPI.SetThreadAffinityMask(currentThreadHandle, mask);
    }

    static void RunTest(int testIndex, int numThreads, ref int totalMigrations, ref double overallTotalDuration, StreamWriter outputFile)
    {
        Thread[] threads = new Thread[numThreads];
        ThreadData[] threadData = new ThreadData[numThreads];

        migrationCount = 0;
        totalMigrationTime = 0;

        for (int i = 0; i < numThreads; i++)
        {
            threadData[i] = new ThreadData
            {
                ThreadId = i,
                CoreToStart = i % 4,       
                CoreToMigrate = (i + 1) % 4 
            };

            threads[i] = new Thread(ThreadFunction);
            threads[i].Start(threadData[i]);
        }

        foreach (var thread in threads)
        {
            thread.Join();
        }

        
        double testTotalDuration = (double)totalMigrationTime / Stopwatch.Frequency * 1e6;
        double testAverageDuration = testTotalDuration / migrationCount;

        outputFile.WriteLine($"{testIndex + 1} {testAverageDuration:F3}");

        overallTotalDuration += testAverageDuration;
        totalMigrations += migrationCount;
    }

    static void Main(string[] args)
    {
        if (args.Length != 2)
        {
            Console.Error.WriteLine("Not enough arguments");
            return;
        }

        int numTests = int.Parse(args[0]);
        int numThreads = int.Parse(args[1]);

        if (numTests <= 0 || numThreads <= 0)
        {
            Console.Error.WriteLine("Both arguments must be positive integers.");
            return;
        }

        string outputPath = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\thread_migration_results.txt";

        using (var outputFile = new StreamWriter(outputPath, true))
        {
            outputFile.WriteLine("C#");

            int totalMigrations = 0;
            double overallTotalDuration = 0.0;

            for (int i = 0; i < numTests; i++)
            {
                RunTest(i, numThreads, ref totalMigrations, ref overallTotalDuration, outputFile);
            }

            double overallAverageDuration = overallTotalDuration / numTests;
            outputFile.WriteLine($"Average {overallAverageDuration:F3}");
        }

        Console.WriteLine("Test complete. Results written to file.");
    }

    internal static class WinAPI
    {
        [System.Runtime.InteropServices.DllImport("kernel32.dll")]
        public static extern UIntPtr SetThreadAffinityMask(IntPtr hThread, UIntPtr dwThreadAffinityMask);
    }
}
