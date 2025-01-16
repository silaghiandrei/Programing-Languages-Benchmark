using System;
using System.Diagnostics;
using System.IO;

class Program
{
    const int SIZE = 5000;

    static double GetTime()
    {
        return Stopwatch.GetTimestamp() / (double)Stopwatch.Frequency;
    }

    static void AllocateAndFillArray(Span<int> array)
    {
        for (int i = 0; i < array.Length; i++)
        {
            array[i] = i;
        }
    }

    static double MeasureLoopExecution(Span<int> array)
    {
        double startTime = GetTime();
        for (int j = 0; j < array.Length; j++)
        {
            if (array[j] == 0)
            {
            }
        }
        double endTime = GetTime();
        return (endTime - startTime) * 1_000_000;
    }

    static double RunTest(StreamWriter file, int testIndex)
    {
        Span<int> array = stackalloc int[SIZE];
        AllocateAndFillArray(array);
        double duration = MeasureLoopExecution(array);
        if (testIndex >= 5)
        {
            file.WriteLine($"{testIndex + 1 - 5} {duration:F6}");
            return duration;
        }
        else
        {
            return 0;
        }
    }

    static void Main(string[] args)
    {
        if (args.Length < 1)
        {
            Console.WriteLine("Not enough arguments");
            return;
        }

        string filePath = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\static_reading_results.txt";
        using StreamWriter file = new(filePath, true);

        if (file == null)
        {
            Console.WriteLine("Error opening the file");
            return;
        }

        int testsNo = int.Parse(args[0]);
        double sum = 0.0;

        file.WriteLine("C#");

        for (int i = 0; i < testsNo + 5; i++)
        {
            sum += RunTest(file, i);
        }

        double average = sum / testsNo;
        file.WriteLine($"Average {average:F3}");
    }
}
