using System;
using System.Threading.Tasks;

namespace PolynomialMultiplication
{
    class Program
    {
        static void Main(string[] args)
        {

            // Convert the coefficients to integers
            int[] intCoefficients1 = new int[] { 1, 1, 1};

            // Convert the coefficients to integers
            int[] intCoefficients2 = new int[] { 0, 0, 1 };

            // Calculate the result of multiplying the two polynomials
            int[] result = MultiplyPolynomials(intCoefficients1, intCoefficients2);

            // Print the result
            Console.WriteLine("The result is: ");
            for (int i = 0; i < result.Length; i++)
            {
                Console.Write(result[i] + " ");
            }
            Console.WriteLine();
        }

        static int[] MultiplyPolynomials(int[] coefficients1, int[] coefficients2)
        {

            if (coefficients1.Length != coefficients2.Length)
            {
                throw new InvalidOperationException("Karatsuba must be applied on polynomials of the same degree!");
            }

            int degree = coefficients1.Length - 1;
            // If either polynomial is of degree 0, return the other polynomial
            if (degree == 0)
            {
                return new int[] { coefficients1[0] * coefficients2[0] };
            }

            // Calculate the degree of the result
            int result_degree = 2 * degree;

            // Calculate the length of the left and right halves of the polynomials
            int halfLength = (degree + 1) / 2;

            // Create arrays to hold the left and right halves of the first polynomial

            // TODO: check if this is right
            int[] left1 = new int[halfLength];
            int[] right1 = new int[degree - halfLength + 1];

            // Copy the left and right halves of the first polynomial into the appropriate arrays
            Array.Copy(coefficients1, 0, left1, 0, halfLength);
            Array.Copy(coefficients1, halfLength, right1, 0, degree - halfLength + 1);

            // Create arrays to hold the left and right halves of the second polinomial
            int[] left2 = new int[halfLength];
            int[] right2 = new int[degree - halfLength + 1];
            // Copy the left and right halves of the second polynomial into the appropriate arrays
            Array.Copy(coefficients2, 0, left2, 0, halfLength);
            Array.Copy(coefficients2, halfLength, right2, 0, degree - halfLength + 1);

            // Create a task to calculate the product of the left halves of the two polynomials
            Task<int[]> task1 = Task.Run(() => MultiplyPolynomials(left1, left2));

            // Create a task to calculate the product of the right halves of the two polynomials
            Task<int[]> task2 = Task.Run(() => MultiplyPolynomials(right1, right2));

            // Create a task to calculate the product of the sum of the left and right halves of the first
            // polynomial and the sum of the left and right halves of the second polynomial
            Task<int[]> task3 = Task.Run(() => MultiplyPolynomials(AddPolynomials(left1, right1),
                                                                    AddPolynomials(left2, right2)));

            // Wait for the tasks to complete
            Task.WaitAll(task1, task2, task3);

            // Get the results of the tasks
            // D_0 * E_0 
            int[] result1 = task1.Result;

            // D_1 * E_1
            int[] result2 = task2.Result;

            // (E_1 + E_0) * (D_1 + D_0)
            int[] result3 = task3.Result;

            int pow = degree / 2 + 1;
            int[] r1 = MultiplyPolynomialByPower(result1, 2 * pow);
            PrintPolynomial(r1);
            int[] r2 = MultiplyPolynomialByPower(
                SubtractPolynomials(SubtractPolynomials(result3, result1), result2), pow);
            PrintPolynomial(r2);
            int[] r3 = result1;
            PrintPolynomial(r3);
            // Return the result

            return AddPolynomials(AddPolynomials(r1, r2), r3);
        }

        static void PrintPolynomial(int[] coefficients)
        {
            for (int i = 0; i < coefficients.Length; i++)
            {
                Console.Write(coefficients[i] + " ");
            }
            Console.WriteLine();
        }

        static int[] MultiplyPolynomialByPower(int[] coefficients, int power)
        {
            int[] result = new int[coefficients.Length + power];
            Array.Copy(coefficients, 0, result, power, coefficients.Length);
            return result;
        }

        // This method adds two polynomials together and returns the result
        static int[] AddPolynomials(int[] coefficients1, int[] coefficients2)
        {
            // Calculate the maximum length of the two polynomials
            int maxLength = Math.Max(coefficients1.Length, coefficients2.Length);

            // Create an array to hold the result
            int[] result = new int[maxLength];

            // Add the coefficients of the two polynomials together and store the result in the result array
            for (int i = 0; i < maxLength; i++)
            {
                result[i] = (i < coefficients1.Length ? coefficients1[i] : 0) +
                            (i < coefficients2.Length ? coefficients2[i] : 0);
            }

            // Return the result
            return result;
        }

        static int[] SubtractPolynomials(int[] coefficients1, int[] coefficients2)
        {
            // Calculate the maximum length of the two polynomials
            int maxLength = Math.Max(coefficients1.Length, coefficients2.Length);

            // Create an array to hold the result
            int[] result = new int[maxLength];

            // Add the coefficients of the two polynomials together and store the result in the result array
            for (int i = 0; i < maxLength; i++)
            {
                result[i] = (i < coefficients1.Length ? coefficients1[i] : 0) -
                            (i < coefficients2.Length ? coefficients2[i] : 0);
            }

            // Return the result
            return result;
        }
    }
}
