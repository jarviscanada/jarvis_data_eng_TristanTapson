package ca.jrvs.practice.codingChallenge;

import java.util.HashMap;
import java.util.Map;

/**
 * ticket: https://www.notion.so/jarvisdev/Valid-Anagram-b8708bd705574ecfa14ebf5cbb759c95
 * description:
 * Given two strings s and t, return true if t is an anagram of s, and false otherwise.
 *
 * An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.
 */
public class ValidAnagram {

    /**
     * Big-O: O(n)
     * Justification: Takes a string converted to character array, then simply maps
     *                the string by <character, character count>, cnd checks if the
     *                maps for the two strings are equal. Since we must iterate
     *                through each character to map it, time complexity is in O(n).
     */

    // main
    public static void main(String[] args) {

        // testing
        System.out.println("--- ValidAnagram Class ---");
        System.out.println("anagram?: " + isAnagram("anagram ","naga ram"));
        System.out.println("anagram?: " + isAnagram("rat","car"));
    }


    // helper function - maps a string; key/val = char/char count
    public static Map<Character, Integer> getStrMap(String s){
        HashMap<Character, Integer> strMap = new HashMap<Character, Integer>();

        // add key (character) and value (character count) to map if key not already in map
        for(char ch : s.toCharArray()){
            if(!strMap.containsKey(ch)){
                strMap.put(ch, 1);
            }

            // key exists? add to the character count
            else{
                strMap.put(ch, strMap.get(ch) + 1);
            }
        }
        return strMap;
    }

    // uses .equals between two mapped strings to check for anagram match
    public static boolean isAnagram(String s, String t) {

        HashMap<Character, Integer> strMap1 = (HashMap<Character, Integer>) getStrMap(s);
        HashMap<Character, Integer> strMap2 = (HashMap<Character, Integer>) getStrMap(t);

        System.out.println("map for string '" + s + "': " + strMap1);
        System.out.println("map for string '" + t + "': " + strMap2);

        // anagram check
        if(strMap1.equals(strMap2)){
            return true;
        }

        return false;
    }
}
