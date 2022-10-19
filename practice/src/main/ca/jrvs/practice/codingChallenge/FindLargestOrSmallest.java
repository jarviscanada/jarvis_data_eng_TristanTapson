package ca.jrvs.practice.codingChallenge;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ticket: https://www.notion.so/jarvisdev/Find-Largest-Smallest-29c67f094dd84dcd9d2c84383c2a1b24
 *
 * description: TODO - not on leetcode...
 * */

public class FindLargestOrSmallest {

    // main
    public static void main(String[] args) {

        // testing
        int[] arr = {1,2,3,4,0,-1,-2,9,8,7,6};

        System.out.println("array: " + Arrays.toString(arr));
        System.out.println("min/max using for loop: " +
                Arrays.toString(findLargestSmallestF(arr)));
        System.out.println("min/max using streams: " +
                Arrays.toString(findLargestSmallestS(arr)));
        System.out.println("min/max using collections: " +
                Arrays.toString(findLargestSmallestC(arr)));

    }

    // for loop approach
    public static int[] findLargestSmallestF(int[] arr){

        int[]  results = new int[2];

        int smallest = arr[0];
        int largest = arr[0];

        for(int i = 0; i < arr.length; i++){

            // update current smallest
            if(arr[i] < smallest){
                smallest = arr[i];
            }

            // update current largest
            if(arr[i] > largest){
                largest = arr[i];
            }
        }

        results[0] = smallest;
        results[1] = largest;

        return results;
    }

    // stream approach
    public static int[] findLargestSmallestS(int[] arr){

        int[] results = new int[2];

        // convert array to list
        List<Integer> list = Arrays.stream(arr)
                .boxed()
                .collect(Collectors.toList());

        // get max/min from stream
        results[0] = list.stream()
                .min(Comparator.comparing(Integer::valueOf)).get();
        results[1] = list.stream()
                .max(Comparator.comparing(Integer::valueOf)).get();

        return results;
    }

    // collections approach
    public static int[] findLargestSmallestC(int[] arr){

        int[] results = new int[2];

        // convert array to list
        List<Integer> list = Arrays.stream(arr)
                .boxed()
                .collect(Collectors.toList());

        // collections to get min/max
        results[0] = Collections.min(list);
        results[1] = Collections.max(list);

        return results;
    }
}