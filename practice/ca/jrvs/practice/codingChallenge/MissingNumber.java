package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * ticket: https://www.notion.so/jarvisdev/Missing-Number-14168f5163274087ab9d46452f00b18b
 *
 * description: Given an array nums containing n distinct numbers in the
 *              range [0, n], return the only number in the range that is
 *              missing from the array.
 * */

public class MissingNumber {

    // TODO: time complexity / justification

    /**
     * Big-O: O(n)
     * Justification:
     */

    // main
    public static void main(String[] args) {

        // testing
        int[] testArr = {3,0,1};
        System.out.println(Arrays.toString(testArr));
        System.out.println("missing number: " + missingNumber(testArr));
    }

    public static int missingNumber(int[] nums) {

        int min = 0;
        int max = getMax(nums);
        System.out.println("min: " + min + ", max: " + max);
        Set<Integer> numberSet = new TreeSet<Integer>();

        // add all numbers in the input array to a set
        for(int num : nums){
            numberSet.add(num);
        }

        // iterate from min to max, and return the value not in the set
        for(int i = min; i <= max; i++){
            if(!numberSet.contains(i)){
                return i;
            }
        }

        // otherwise return max val + 1
        return (max + 1);

    }

    // helper function - returns the maximum number in an array
    public static int getMax(int[] nums){

        int max = nums[0];

        for(int num : nums){
            if (num > max){
                max = num;
            }
        }

        return max;
    }
}
