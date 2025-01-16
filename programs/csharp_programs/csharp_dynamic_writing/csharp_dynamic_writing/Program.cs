using System;
using System.Diagnostics;
using System.IO;
using System.Numerics;

class Program
{
    static double GetTime()
    {
        return Stopwatch.GetTimestamp() / (double)Stopwatch.Frequency;
    }

    static double MeasureLoopExecution(object[] array)
    {
        double startTime = GetTime();
        for (int j = 0; j < array.Length; j++)
        {
            array[j] = j;
        }
        double endTime = GetTime();
        return (endTime - startTime) * 1_000_000;
    }

    static double RunTest(StreamWriter file, int testIndex, int size)
    {
        object[] array = new object[size];
        double duration = MeasureLoopExecution(array);
        if (testIndex >= 10)
        {
            file.WriteLine($"{testIndex + 1 - 10} {duration:F6}");
            return duration;
        }
        else 
        {
            return 0;
        }
    }

    static void Main(string[] args)
    {
        if (args.Length < 2)
        {
            Console.WriteLine("Not enough arguments");
            return;
        }

        string filePath = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\dynamic_writing_results.txt";
        using StreamWriter file = new(filePath, true);

        if (file == null)
        {
            Console.WriteLine("Error opening the file");
            return;
        }

        int testsNo = int.Parse(args[0]);
        int arraySize = int.Parse(args[1]);
        double sum = 0.0;

        file.WriteLine("C#");

        for (int i = 0; i < testsNo + 10; i++)
        {
            sum += RunTest(file, i, arraySize);
        }

        double average = sum / testsNo;
        file.WriteLine($"Average {average:F3}");
    }
}
