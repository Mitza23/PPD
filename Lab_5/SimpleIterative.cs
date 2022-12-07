using System;

namespace ConsoleApp
{
    class SimpleIterative
    {

        // the degree of the polynomials
        public static int DEGREE;

        // the coefficients of the first polynomial
        public static int[] COEFFICIENTS_1;

        // the coefficients of the second polynomial
        public static int[] COEFFICIENTS_2;

        // a helper function that multiplies the polynomial with coefficient c1 at degree d1 with the polynomial with coefficient c2 at degree d2 and adds the result to the results array
        public static void MultiplyDegree(int degree, int[] coefficients1, int[] coefficients2, int[] results)
        {
            for (int i = 0; i <= DEGREE; i++)
            {
                int result = coefficients1[degree] * coefficients2[i];
                int index = degree + i;
                results[index] += result;
            }
        }

        public static void Multiply(int[] coefficients1, int[] coefficients2, int[] results, int DEGREE)
        {
            for (int i = 0; i <= DEGREE; i++)
            {
                int degree = i;
                MultiplyDegree(degree, COEFFICIENTS_1, COEFFICIENTS_2, results);
            }
        }

        static void work()
        {
            DEGREE = 4;
            COEFFICIENTS_1 = new int[] { 1, 2, 3, 4, 5 };
            COEFFICIENTS_2 = new int[] { 5, 2, 3, 4, 0 };

            // create an array to hold the results of the multiplication
            int[] results = new int[DEGREE * 2 + 1];

            // create an array of tasks to perform the multiplication in parallel

            Multiply(COEFFICIENTS_1, COEFFICIENTS_2, results, DEGREE);

            // print the results of the multiplication
            Console.Write("(");
            for (int i = 0; i <= DEGREE; i++)
            {
                Console.Write($"{COEFFICIENTS_1[i]}x^{DEGREE - i}");
                if (i < DEGREE)
                {
                    Console.Write(" + ");
                }
            }
            Console.Write(") * (");
            for (int i = 0; i <= DEGREE; i++)
            {
                Console.Write($"{COEFFICIENTS_2[i]}x^{DEGREE - i}");
                if (i < DEGREE)
                {
                    Console.Write(" + ");
                }
            }
            Console.WriteLine(") =");
            Console.Write("    ");
            for (int i = 0; i <= DEGREE * 2; i++)
            {
                Console.Write($"{results[i]}x^{DEGREE * 2 - i}");
                if (i < DEGREE * 2)
                {
                    Console.Write(" + ");
                }
            }
            Console.WriteLine();
        }

        //static void Main(string[] args)
        //{
        //    work();
        //}


    }
}
