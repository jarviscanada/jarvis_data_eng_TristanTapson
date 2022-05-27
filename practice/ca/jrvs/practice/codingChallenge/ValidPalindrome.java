package ca.jrvs.practice.codingChallenge;

import static java.lang.Character.toLowerCase;

/**
 * ticket: https://www.notion.so/jarvisdev/Valid-Palindrome-28f431e7be9f4e888658b9f3ebdca03f
 * description:
 * A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward. Alphanumeric characters include letters and numbers.
 *
 * Given a string s, return true if it is a palindrome, or false otherwise.
 */
public class ValidPalindrome {

/**
 * Big-O: O(n)
 * Justification: To filter the string for correct ascii characters, we have to
 *                iterate through the string once. Then we iterate through the
 *                string again to check if the leftmost/rightmost characters
 *                align. Technically O(2n), but O(n) = O(cn) for any fixed c > 0,
 *                so time complexity is O(n).
 *
 *                NOTE: this was done using the two pointers approach (start/end index)
 */

    // main
    public static void main(String[] args) {

        // testing
        System.out.println("--- ValidPalindrome Class ---");
        String str1 = "A man, a plan, a canal: Panama";
        System.out.println("string: " + str1);
        System.out.println("palindrome?: " + isPalindrome(str1));
    }

    // checks if a string is a palindrome by walking through leftmost/rightmost string characters
    public static boolean isPalindrome(String s) {

        StringBuilder compareString = filterString(s);
        int start = 0;
        int end = compareString.length() - 1;

        // compare the first and last characters of the string
        while(start < end){

            // disprove by contradiction; leftmost/rightmost characters dont match once? not a palindrome
            if(compareString.charAt(start) != compareString.charAt(end)){
                return false;
            }

            // continue walking through string checking leftmost/rightmost characters otherwise
            start++;
            end--;
        }

        return true;
    }

    // helper function - formats the string to valid ascii characters
    public static StringBuilder filterString(String s){

        StringBuilder newStr = new StringBuilder();
        for(char ch : s.toCharArray()) {
            if((ch >= 48 && ch <= 57) // ascii 0-9
                    || (ch >= 65 && ch <= 90) // ascii A-Z
                    || (ch >= 97 && ch <= 122)){ // ascii a-z
                newStr.append(toLowerCase(ch));
            }
        }

        System.out.println("filtered string: "+ newStr.toString());
        return newStr;
    }
}
