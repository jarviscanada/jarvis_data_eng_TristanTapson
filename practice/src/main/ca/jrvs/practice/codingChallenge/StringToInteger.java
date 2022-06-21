package ca.jrvs.practice.codingChallenge;

import java.util.ArrayList;
import java.util.List;

// TODO: still in progress...

public class StringToInteger {

    // main
    public static void main(String[] args) {

        // testing
        String testStr = "      -4509 here are some words...";
        System.out.println("atoi: " + myAtoi(testStr));
    }

    // ignore whitespace, non digits
    // assume positive if + or - sign not in the string

    public static int myAtoi(String s) {

        StringBuilder newStr = strFormatter(s);
        int value = getValueOfString(newStr.toString());
        if(isPositive(s) == false){
            value *= -1;
        }

        return value;
    }

    public static boolean isPositive(String s){

        // search for negative sign in the string
        for(char ch : s.toCharArray()){
            if(ch == '-'){
                return false;
            }
        }

        // otherwise
        return true;
    }

    public static StringBuilder strFormatter(String s){
        StringBuilder newStr = new StringBuilder();
        for(char ch : s.toCharArray()){
            if(ch >= 48 && ch <= 57){ // ascii 0-9
                newStr.append(ch);
            }
        }

        // System.out.println(newStr);
        return newStr;
    }

    // after formatted, get numeric val
    public static int getValueOfString(String s){

        // (length - curr index - 1) * (int) char

        int total = 0;
        final int asciiOffset = 48;

        for(int i = 0; i < s.length(); i++){

            double expo = s.length() - i - 1;
            int mul = (int) Math.pow(10,expo);
            total += mul * ((int)s.charAt(i) - asciiOffset);
        }

        return total;
    }
}