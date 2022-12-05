using System;
using System.Threading.Tasks;

namespace PolynomialMultiplication
{
    class Program
    {
        static void Main(string[] args)
        {
            // Define the coefficients of the two polynomials to be multiplied
            int[] a = { 1, 2, 3 }; // x^2 + 2x + 3
            int[] b = { 4, 5, 6 }; // 4x^2 + 5x + 6

            // Calculate the product of the two polynomials using the Karatsuba algorithm
            int[] product = MultiplyPolynomials(a, b);

            // Print the result
            Console.Write("Product: ");
            for (int i = 0; i < product.Length; i++)
            {
                Console.Write(product[i] + "x^" + i + " + ");
            }
        }

        // Function to multiply two polynomials using the Karatsuba algorithm in parallel
        static int[] MultiplyPolynomials(int[] a, int[] b)
        {
            // Make sure the arrays have the same length
            int n = Math.Max(a.Length, b.Length);
            Array.Resize(ref a, n);
            Array.Resize(ref b, n);

            // Calculate the product of the two polynomials using the Karatsuba algorithm
            int[] product = new int[2 * n - 1];
            if (n == 1)
            {
                product[0] = a[0] * b[0];
            }
            else
            {
                // Divide the polynomials into two halves
                int[] a1 = new int[n / 2];
                int[] a2 = new int[n / 2];
                int[] b1 = new int[n / 2];
                int[] b2 = new int[n / 2];
                Array.Copy(a, 0, a1, 0, n / 2);
                Array.Copy(a, n / 2, a2, 0, n / 2);
                Array.Copy(b, 0, b1, 0, n / 2);
                Array.Copy(b, n / 2, b2, 0, n / 2);

                // Use tasks to calculate the terms of the product in parallel
                Task<int[]> t1 = Task.Run(() => MultiplyPolynomials(a1, b1));
                Task<int[]> t2 = Task.Run(() => MultiplyPolynomials(a2, b2));
                Task<int[]> t3 = Task.Run(() => MultiplyPolynomials(AddPolynomials(a1, a2), AddPolynomials(b1, b2)));

                // Wait for all tasks to finish
                Task.WaitAll(t1, t2, t3);

                // Compute the product using the Karatsuba formula
                int[] term1 = t1.Result;
                int[] term2 = t2.Result;
                int[] term3 = t3.Result;
                for (int i = 0; i < term1.Length; i++)
                {
                    product[i] += term1[i];
                }
                for (int i = 0; i < term2.Length; i++)
                {
                    product[i + n] += term2[i];
                }
                for (int i = 0; i < term3.Length; i++)
                {
                    product[i + n / 2] += term3[i] - term1[i] - term2[i];
                }
            }

            return product;
        }
        // Function to add two polynomials
        static int[] AddPolynomials(int[] a, int[] b)
        {
            // Make sure the arrays have the same length
            int n = Math.Max(a.Length, b.Length);
            Array.Resize(ref a, n);
            Array.Resize(ref b, n);

            // Add the two polynomials and return the result
            int[] sum = new int[n];
            for (int i = 0; i < n; i++)
            {
                sum[i] = a[i] + b[i];
            }
            return sum;
        }
    }
}