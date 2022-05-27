package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;

/**
 * ticket: https://www.notion.so/jarvisdev/Fibonacci-Number-Climbing-Stairs-b626feea76a246f6bc49b872b980f4bb
 *
 * description: The Fibonacci numbers, commonly denoted F(n) form a sequence,
 *              called the Fibonacci sequence, such that each number is the sum
 *              of the two preceding ones, starting from 0 and 1. That is:
 *
 * F(0) = 0, F(1) = 1
 * F(n) = F(n - 1) + F(n - 2), for n > 1.
 * Given n, calculate F(n).
 *
 * NOTE: recursive, iterative, and dynamic approach are implemented
 */

public class FibonacciNumber {

/**
 * Big-O: O(2^n) - recursive, O(n) - iterative/dynamic
 * Justification: Recursion shouldn't really be used if we can do the same task
 *                iteratively or dynamically, as the stack can only contain so
 *                many function calls. With the iterative & dynamic approach, we
 *                are calculating the fib number by either iterating from 0 to n
 *                and updating the 2 values needed for calculation, or by storing
 *                a value in an array to eliminate unnecessary recursive calls,
 *                so time complexity is O(n).
 */

    // main
    public static void main(String[] args) {

        // testing
        System.out.println("--- FibonacciNumber Class ---");
        int num = 8;
        System.out.println("Computing fib(" + num +"):");
        System.out.println("recursive = " + fibR(num));
        System.out.println("iterative = " + fibI(num));
        System.out.println("dynamic = " + fibD(num));
    }

    // recursive fibonacci approach
    public static int fibR(int n) {

            // base cases
            if(n == 0) return 0;
            if(n == 1) return 1;

            return fibR(n-1) + fibR(n-2);
    }

    // iterative fibonacci approach
    public static int fibI(int n){

        int first = 0;
        int next = 1;
        int sum = 0;

        // base cases
        if(n == 0) return 0;
        if(n == 1) return 1;

        else {
            // iterate to update sum
            for (int i = 0; i < n - 1; i++) {
                sum = first + next;
                first = next;
                next = sum;
            }
        }

        return sum;
    }

    // dynamic fibonacci approach
    public static int fibD(int n){

        final int max = n+1;
        int[] memoArr = new int[max];

        // base cases
        if(n == 0) return 0;
        if(n == 1) return 1;

        // return result stored in memo array; gets rid of repeat recursive calls
        if(memoArr[n] != 0) return memoArr[n];

        // store the result in the memo array
        memoArr[n] = fibD(n - 1) + fibD(n - 2);
        return memoArr[n];
    }
}
