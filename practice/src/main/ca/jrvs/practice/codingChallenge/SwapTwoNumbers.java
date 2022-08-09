package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;

/**
 * ticket: https://www.notion.so/jarvisdev/Swap-two-numbers-8ce001b1988e408a941678732ea34cb1
 *
 * description: Swap two numbers without using the third variable
 * */

public class SwapTwoNumbers {

    // main
    public static void main(String[] args) {

        // testing
        int[] arr = {2,3};

        System.out.println("array before swap: " + Arrays.toString(arr));
        System.out.println("array after arithmetic swap: "
                + Arrays.toString(swapTwoArithmetic(arr)));

        System.out.println("array before swap: " + Arrays.toString(arr));
        System.out.println("array after bitwise swap: "
                + Arrays.toString(swapTwoBitwise(arr)));
    }

    // TODO: time complexity / justification

    /**
     * Big-O: O(n)
     * Justification:
     */


    // arithmetic operators approach
    public static int[] swapTwoArithmetic(int[] arr){

        arr[0] = arr[0] + arr[1];
        arr[1] = arr[0] - arr[1];
        arr[0] = arr[0] - arr[1];

        return arr;
    }

    // bitwise operators approach
    public static int[] swapTwoBitwise(int[] arr){

        arr[0] = arr[0] ^ arr[1];
        arr[1] = arr[0] ^ arr[1];
        arr[0] = arr[0] ^ arr[1];

        return arr;
    }
}
