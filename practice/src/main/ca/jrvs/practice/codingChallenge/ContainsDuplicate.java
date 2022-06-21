package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * ticket: https://www.notion.so/jarvisdev/Contains-Duplicate-f36c4008543d42078e31ee592e20aa61
 *
 * description: Given an integer array nums, return true if any value appears
 *              at least twice in the array, and return false if every element
 *              is distinct.
 * */

public class ContainsDuplicate {

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
                " duplicate(s) found? = " + (containsDuplicate(numArr1)));
        System.out.println(Arrays.toString(numArr2) +
                " duplicate(s) found? = " + (containsDuplicate(numArr2)));
    }

    public static boolean containsDuplicate(int[] nums){

        Set<Integer> numSet = new HashSet<Integer>();

        for(int num : nums){

            // check to see if the num already exists in the set
            if(numSet.contains(num)){
                return true;
            }

            // otherwise add the num to the set
            numSet.add(num);
        }

        return false;
    }
}
