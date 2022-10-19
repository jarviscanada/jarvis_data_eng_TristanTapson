package ca.jrvs.practice.codingChallenge;

/**
 * ticket: https://www.notion.so/jarvisdev/Rotate-String-952107e8f3594307851e34a6173c076a
 *
 * description: Given two strings s and goal, return true if and only if s can
 *              become goal after some number of shifts on s.
 *
 *              A shift on s consists of moving the leftmost character of s to
 *              the rightmost position.
 *
 *              For example, if s = "abcde", then it will be "bcdea" after one
 *              shift.
 * */

public class RotateString {

    // TODO: time complexity / justification

    /**
     * Big-O: O(n)
     * Justification:
     */

    // main
    public static void main(String[] args) {

        // testing
        String s1 = "abcde"; String goal1 = "cdeab";
        String s2 = "abcde"; String goal2 = "abced";

        System.out.println(s1 + " & " + goal1 + " = " + rotateString(s1, goal1));
        System.out.println(s2 + " & " + goal2 + " = " + rotateString(s2, goal2));
    }

    public static boolean rotateString(String s, String goal) {

        // strings not same length
        if(s.length() != goal.length()){
            return false;
        }

        // adding the strings multiple times to "rotate" it
        String shiftString = s + s + s;
        // System.out.println(shiftString);

        // then just check if the rotated string contains the goal
        if(shiftString.contains(goal)){
            return true;
        }

        return false;
    }
}
