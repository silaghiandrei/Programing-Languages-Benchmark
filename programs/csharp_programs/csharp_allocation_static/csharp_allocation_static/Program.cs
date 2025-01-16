using System;
using System.Diagnostics;
using System.IO;

class Program
{
    const int SIZE = 1000; 

    static double GetTime()
    {
        return Stopwatch.GetTimestamp() / (double)Stopwatch.Frequency;
    }

    static double PerformTest(int testNumber, StreamWriter file)
    {
        double startTime = GetTime();

        Span<int> span = stackalloc int[SIZE];

        double endTime = GetTime();
        double duration = (endTime - startTime) * 1_000_000;

        UseFunction(span, SIZE);

        if (testNumber >= 5)
        {
            file.WriteLine($"{testNumber - 4} {duration:F6}");
            return duration;
        }
        else
        {
            return 0;
        }
    }

    static void UseFunction(Span<int> span, int size)
    {
        for (int i = 0; i < size; i++)
        {
            span[i] = i;
        }
    }

    static void Main(string[] args)
    {
        if (args.Length < 1)
        {
            Console.WriteLine("Not enough arguments");
            return;
        }

        int testsNo;
        if (!int.TryParse(args[0], out testsNo))
        {
            Console.WriteLine("Invalid argument");
            return;
        }

        string filePath = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\static_allocation_results.txt";

        try
        {
            using (StreamWriter file = new StreamWriter(filePath, append: true))
            {
                file.WriteLine("C#");

                double totalDuration = 0;

                for (int i = 0; i < testsNo + 5; i++)
                {
                    totalDuration += PerformTest(i, file);
                }

                file.WriteLine($"Average {totalDuration / testsNo:F3}");
            }

            Console.WriteLine($"Results written to {filePath}");
        }
        catch (IOException ex)
        {
            Console.WriteLine($"Error writing to file: {ex.Message}");
        }
    }
}
