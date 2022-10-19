package ca.jrvs.practice.codingChallenge;

import java.util.regex.Pattern;

/**
 * ticket: https://www.notion.so/jarvisdev/Check-if-a-String-contains-only-digits-13bc5cf4078247aea436555247186208
 *
 * description: TODO - not on leetcode...
 * */

public class StringContainsOnlyDigits {

    // main
    public static void main(String[] args) {

        // testing
        String str1 = "1234";
        String str2 = "123,000";

        // ascii test
        System.out.println(str1 + " (ASCII): " + onlyDigitsA(str1));
        System.out.println(str2 + " (ASCII): " + onlyDigitsA(str2));

        // Java API test
        System.out.println(str1 + " (Java API): " + onlyDigitsI(str1));
        System.out.println(str2 + " (Java API): " + onlyDigitsI(str2));

        // regex test
        System.out.println(str1 + " (regex): " + onlyDigitsR(str1));
        System.out.println(str2 + " (regex): " + onlyDigitsR(str2));
    }

    // TODO: time complexity / justification

    /**
     * Big-O: O(n)
     * Justification:
     */

    // ASCII approach
    public static boolean onlyDigitsA(String s){

        // iterate through each character until a non-digit is found
        for(char ch : s.toCharArray()){
            if(!((ch >= 48) && (ch <= 57))){ // ascii 0-9
                // System.out.println("illegal char: " + ch);
                return false;
            }
        }

        // otherwise
        return true;
    }

    // Java API approach
    public static boolean onlyDigitsI(String s){

        for(char ch : s.toCharArray()){
            int val = Integer.valueOf(ch);
            if(!((val >= 48) && (val <= 57))){
                return false;
            }
        }

        // otherwise
        return true;
    }

    // regex approach
    public static boolean onlyDigitsR(String s){

        // regex check for only digits
        if(Pattern.matches("^[0-9]*$", s)){
            return true;
        }

        // otherwise
        return false;
    }
}