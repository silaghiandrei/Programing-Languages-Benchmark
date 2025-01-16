using System;
using System.Diagnostics;
using System.IO;
using System.Threading;

class Program
{
    static double GetTime()
    {
        return Stopwatch.GetTimestamp() / (double)Stopwatch.Frequency;
    }

    static void ThreadFunction()
    {
        return;
    }

    static double MeasureThreadCreationTime(int numThreads)
    {
        Thread[] threads = new Thread[numThreads];

        double totalCreationTime = 0.0;

        for (int i = 0; i < numThreads; i++)
        {
            double startTime = GetTime();
            threads[i] = new Thread(ThreadFunction);
            threads[i].Start();
            double endTime = GetTime();

            totalCreationTime += (endTime - startTime) * 1_000_000;
        }

        for (int i = 0; i < numThreads; i++)
        {
            threads[i]?.Join();
        }

        return totalCreationTime;
    }

    static double RunTest(StreamWriter file, int testIndex, int numThreads)
    {
        double duration = MeasureThreadCreationTime(numThreads);
        file.WriteLine($"{testIndex + 1} {duration:F6}");
        return duration;
    }

    static void Main(string[] args)
    {
        if (args.Length != 2)
        {
            Console.WriteLine("Not enough arguments");
            return;
        }

        if (!int.TryParse(args[0], out int numTests) || !int.TryParse(args[1], out int numThreads) || numThreads <= 0 || numTests <= 0)
        {
            Console.WriteLine("Both arguments must be positive integers.");
            return;
        }

        string filePath = @"E:\Facultate\3-sem1\SSC\proiect\programs\results\thread_creation_results.txt";

        using StreamWriter file = new(filePath, true);

        file.WriteLine("C#");
        double totalTime = 0.0;

        for (int i = 0; i < numTests; i++)
        {
            totalTime += RunTest(file, i, numThreads);
        }

        double averageTime = totalTime / numTests;
        file.WriteLine($"Average {averageTime:F3}");
    }
}
