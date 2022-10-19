package ca.jrvs.practice.codingChallenge;

import java.util.ArrayList;
import java.util.List;

/**
 * ticket: https://www.notion.so/jarvisdev/String-to-Integer-atoi-1d137a35c9bb4613ae89761ff0a3fe13
 *
 * description: Implement the myAtoi(string s) function, which converts a string to a 32-bit signed integer (similar to C/C++'s atoi function).
 *
 *              The algorithm for myAtoi(string s) is as follows:
 *
 *                  Read in and ignore any leading whitespace.
 *                  Check if the next character (if not already at the end of the string) is '-' or '+'. Read this character in if it is either. This determines if the final result is negative or positive respectively. Assume the result is positive if neither is present.
 *                  Read in next the characters until the next non-digit character or the end of the input is reached. The rest of the string is ignored.
 *                  Convert these digits into an integer (i.e. "123" -> 123, "0032" -> 32). If no digits were read, then the integer is 0. Change the sign as necessary (from step 2).
 *                  If the integer is out of the 32-bit signed integer range [-231, 231 - 1], then clamp the integer so that it remains in the range. Specifically, integers less than -231 should be clamped to -231, and integers greater than 231 - 1 should be clamped to 231 - 1.
 *                  Return the integer as the final result.
 * */

public class StringToInteger {

    // TODO: time complexity / justification

    /**
     * Big-O: O(n)
     * Justification:
     */

    // main
    public static void main(String[] args) {

        // testing
        String testStr0 = "";
        String testStr1 = "4";
        String testStr2 = "+4";
        String testStr3 = "-4";
        String testStr4 = "-42 these are some words...";

        System.out.println("atoi: " + myAtoi(testStr4));
    }

    // ignore whitespace, non digits
    // assume positive if + or - sign not in the string

    public static int myAtoi(String s) {

        boolean negative = false;

        // chop whitespace
        String ss = s.trim();
        System.out.println("trimmed str: " + ss);

        // base case empty string
        if(ss.length() == 0){
            System.out.println("length: " + ss.length());
            return 0;
        }

        // base case single character
        if(ss.length() == 1){
            System.out.println("length: " + ss.length());
            if((s.charAt(0) >= 48) && (s.charAt(0) <= 57)){
                return Character.getNumericValue(s.charAt(0));
            }
            else{
                return 0;
            }
        } // otherwise it has more characters...

        // check if negative
        if((ss.charAt(0) == '-')){
            negative = true;
        }

        // negative case
        if(negative){

            System.out.println("length: " + ss.length());
            ss = ss.substring(1);

            char ch = ss.charAt(0);
            if(!((ch >= 48) && (ch <= 57))){
                return 0;
            }

            String result = getString(ss);
            System.out.println("result: " + result);
            long num = Long.parseLong(result);

            System.out.println("within signed range: " + inRange(num));
            if(inRange(num) == true){
                return (int)(num*-1);
            }
            else{
                return Integer.MIN_VALUE;
            }
        }


        // positive case
        else{

            // check if positive sign present or not
            if((ss.charAt(0) == '+')){
                ss = ss.substring(1);
            }

            System.out.println("trimmed plus str: " + ss);
            char ch = ss.charAt(0);
            if(!((ch >= 48) && (ch <= 57))){
                return 0;
            }

            else{
                String result = getString(ss);
                System.out.println("result: " + result);
                long num =  Long.parseLong(result);

                System.out.println("range: " + inRange(num));

                if(inRange(num) == true){
                    return (int)num;
                }
                else{
                    return Integer.MAX_VALUE;
                }

            }

        }
    }

    // helper functions
    public static String getString(String ss){

        String newString = "";
        char[] arr = ss.toCharArray();
        for(char ch : arr){
            if((ch >= 48) && (ch <= 57)){
                // System.out.println("ch: " + ch);
                newString = newString + ch;
            }
            else{
                break;
            }
        }

        return newString;
    }

    static boolean inRange(long num){
        if(Integer.MIN_VALUE <= num && num <= Integer.MAX_VALUE){
            return true;
        }

        else{
            return false;
        }
    }
}