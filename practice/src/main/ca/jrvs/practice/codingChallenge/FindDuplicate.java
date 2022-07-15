package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * ticket: https://www.notion.so/jarvisdev/Find-the-Duplicate-Number-d7cc7d1685c741a09eb7261b58860c6d
 *
 * description: Given an array of integers nums containing n + 1 integers where
 *              each integer is in the range [1, n] inclusive.
 *
 *              There is only one repeated number in nums, return this
 *              repeated number.
 * */

public class FindDuplicate {

    // TODO: time complexity / justification

    /**
     * Big-O: O(n)
     * Justification:
     */


    // main
    public static void main(String[] args) {

        // testing
        int numArr1[] = {1,2,3,1};
        int numArr2[] = {1,2,3,4};

        System.out.println(Arrays.toString(numArr1) +
                " duplicate = " + (findDuplicate2(numArr1)));
    }

    // finding duplicate using sorting
    public static int findDuplicate1(int[] nums){

        // sort the array
        Arrays.sort(nums);

        // iterate through the array
        for(int i = 0; i < nums.length-1; i++){
            int j = i+1;

            // return num if the number next to current index is the same
            if(nums[i] == nums[j]){
                return nums[i];
            }
        }

        // otherwise return -1 if not found
        return -1;
    }

    // finding duplicate using sets
    public static int findDuplicate2(int[] nums){

        Set<Integer> numSet = new HashSet<Integer>();

        // iterate through the list of nums in the array
        for(int num : nums){
            if(numSet.contains(num)) {
                return (num);
            }

            // add the num to the numSet
            numSet.add(num);
        }

        // otherwise return -1 if not found
        return -1;
    }
}
