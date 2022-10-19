package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import java.util.HashMap;

/**
 * ticket: https://www.notion.so/jarvisdev/Two-Sum-4623734546204613ba8acad8795d4d6b
 *
 * description: Given an array of integers nums and an integer target,
 *                  return indices of the two numbers such that they add up to
 *                  target. You may assume that each input would have exactly
 *                  one solution, and you may not use the same element twice.
 *                  You can return the answer in any order.
 * */


public class TwoSum {

    // main
    public static void main(String[] args) {

        // testing
        int numArr1[] = {2,7,11,15}; int target1 = 9;
        int numArr2[] = {3,3}; int target2 = 6;

        System.out.println(Arrays.toString(numArr1) +
                " TwoSum indices = " +
                Arrays.toString(twoSumLinear(numArr1, target1)));
    }

    // TODO: time complexity / justification

    /**
     * Big-O: O(n^2)
     * Justification:
     */

    public static int[] twoSumBrute(int[] nums, int target){

        int[] results = new int[2];

        for(int i = 0; i < nums.length; i++){
            for(int j = 0; j < nums.length; j++){

                // num1 + num2 = target ?
                if(nums[i] + nums[j] == target){

                    // update the result arr to the answer indices
                    results[0] = i;
                    results[1] = j;
                    return results;
                }
            }
        }

        return null;
    }

    // TODO: time complexity / justification

    /**
     * Big-O: O(n)
     * Justification:
     */

    public static int[] twoSumLinear(int[] nums, int target) {

        int[] results = new int[2];

        // num and its index, as a map
        HashMap<Integer, Integer> map = new HashMap<>();

        for(int i = 0; i < nums.length; i++){

            // difference from current num and target
            int diff = target - nums[i];

            // difference exists in the map ?
            if((map.containsKey(diff))){

                // update the result arr to the answer indices
                results[0] = map.get(diff);
                results[1] = i;
                return results;
            }

            // otherwise add the current num to the map
            else {
                map.put(nums[i], i);
            }
        }

        return null;
    }
}
