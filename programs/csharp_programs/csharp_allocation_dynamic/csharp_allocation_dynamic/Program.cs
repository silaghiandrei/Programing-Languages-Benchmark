using System;
using System.Diagnostics;
using System.IO;

class Program
{
    static double GetTime()
    {
        return Stopwatch.GetTimestamp() / (double)Stopwatch.Frequency;
    }

    static void UseFunction(object[] arr, int size)
    {
        for (int i = 0; i < size; i++)
        {
            arr[i] = i;
        }
    }

    static void Main(string[] args)
    {
        if (args.Length < 2)
        {
            Console.WriteLine("Not enough arguments");
            return;
        }

        string filePath = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\dynamic_allocation_results.txt"; 

        try
        {
            using (StreamWriter file = new StreamWriter(filePath, append: true))
            {
                file.WriteLine("C#");

                if (!int.TryParse(args[0], out int testsNo))
                {
                    Console.WriteLine("Invalid tests_no argument");
                    return;
                }

                if (!int.TryParse(args[1], out int arrayLength))
                {
                    Console.WriteLine("Invalid array_length argument");
                    return;
                }

                double sum = 0.0f;

                for (int i = 0; i < testsNo + 5; i++)
                {
                    double startTime = GetTime();

                    object[] array = new object[arrayLength];

                    double endTime = GetTime();
                    double duration = (endTime - startTime) * 1000000;

                    UseFunction(array, arrayLength);

                    if (i >= 5)
                    {
                        sum += duration;
                        file.WriteLine($"{i + 1 - 5:D} {duration:F6}");
                    }
                }

                sum /= testsNo;

                file.WriteLine($"Average {sum:F3}");
                Console.WriteLine($"Results written to {filePath}");
            }
        }
        catch (IOException ex)
        {
            Console.WriteLine($"Error writing to file: {ex.Message}");
        }
    }
}
