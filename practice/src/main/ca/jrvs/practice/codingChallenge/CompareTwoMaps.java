package ca.jrvs.practice.codingChallenge;

import java.util.HashMap;
import java.util.Map;

/**
 * ticket: https://www.notion.so/jarvisdev/How-to-compare-two-maps-187f9bc323f14195949c94bed7a7eb45
 *
 * description: Two maps are equal when they have the same keys and values.
 *              Given two maps, return true if they are equal, and return false
 *              if they are not.
 * */

public class CompareTwoMaps {

    // main
    public static void main(String[] args) {

        // testing
        HashMap<String, String> testMap1 = new HashMap<>();
        testMap1.put("a", "b");
        testMap1.put("c", "d");

        HashMap<String, String> testMap2 = new HashMap<>();
        testMap2.put("c", "d");
        testMap2.put("a", "b");

        HashMap<String, String> testMap3 = new HashMap<>();
        testMap3.put("c", "d");
        testMap3.put("a", "b");
        testMap3.put("e", "f");

        HashMap<String, String> testMap4 = new HashMap<>();
        testMap4.put("a", "c");

        System.out.println(testMap1.toString() + " & " + testMap2.toString()
                + " = " + compareMaps2(testMap1, testMap2));
        System.out.println(testMap1.toString() + " & " + testMap3.toString()
                + " = " + compareMaps2(testMap1, testMap3));
    }

    public static <K,V> boolean compareMaps1(Map<K, V> m1, Map<K, V> m2){

        // maps are equal
        if(m1.equals(m2)){
            return true;
        }

        // otherwise
        return false;
    }

    // TODO: time complexity / justification

    /**
     * Big-O: O(n)
     * Justification:
     */

    public static <K,V> boolean compareMaps2(Map<K, V> m1, Map<K, V> m2){

        // size not equal
        if(m1.size() != m2.size()){
            return false;
        }

        // compare key values
        for(K key : m1.keySet()){
            // key values not equal
            if(!(m1.get(key).equals(m2.get(key)))){
                return false;
            }
        }

        // otherwise
        return true;
    }
}
