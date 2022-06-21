package ca.jrvs.practice.codingChallenge;

import java.util.*;

/**
 * ticket: https://www.notion.so/jarvisdev/Duplicates-from-Sorted-Array-3fc8d44b3c324aafbe21dc548fba3a79
 *
 * description: Given an integer array nums of length n where all the integers
 *              of nums are in the range [1, n] and each integer appears once or
 *              twice, return an array of all the integers that appears twice.
 * */

public class DuplicatesFromSorted {

    // TODO: time complexity / justification

    /**
     * Big-O: O(n)
     * Justification:
     */

    // main
    public static void main(String[] args) {

        // testing (assumption is array is sorted)
        int testArr[] = {4,3,2,7,8,2,3,1};
        Arrays.sort(testArr);
        System.out.println(findDuplicates(testArr));
    }

    public static List<Integer> findDuplicates(int[] nums) {

        Set<Integer> intSet = new HashSet<Integer>();
        HashMap<Integer, Integer> intCountMap = new HashMap<Integer, Integer>();

        // iterate through the list of nums to populate the map
        for(int num : nums){

            // if the integer is not in the map, add it to the map with a count of 1
            if(!intCountMap.containsKey(num)){
                intCountMap.put(num, 1);
            }

            // already in the map? update the count
            else{
                intCountMap.put(num, intCountMap.get(num) + 1);
            }
        }

        // iterate through the list of nums again
        for(int num : nums){
            // if the count of the num is > 1, add it to the set of duplicates
            if(intCountMap.get(num) > 1){
                intSet.add(num);
            }
        }

        // set to list
        List<Integer> listOfDupes = new ArrayList<>(intSet);
        return listOfDupes;
    }
}
