package ca.jrvs.practice.codingChallenge;

/**
 * ticket: https://www.notion.so/Print-letter-with-number-324e4491c5c94144a8846fccbd7a6b04
 *
 * description: Given a String which contains only lower and upper case letters,
 *              print letter index after each character. For example, the index
 *              of a is 1,  b is 2, A is 27, and so on.
 * */

public class PrintLetterWithNumber {

    static final int OFFSET_L = 96; // lowercase offset
    static final int OFFSET_U = 38; // uppercase offset


    // main
    public static void main(String[] args) {

        // testing
        String input = "Abcee";
        System.out.println("input: " + input);
        System.out.println("output: " + print(input));
    }

    // TODO: time complexity / justification

    /**
     * Big-O: O(n)
     * Justification:
     */

    public static String print(String input){

        String output = "";

        for(char ch : input.toCharArray()){
            // System.out.println(Integer.valueOf(ch) - OFFSET);

            // lowercase letters
            if(ch >= 'a' && ch <= 'z') {

                // add numeric val next to letter in string
                int num = Integer.valueOf(ch) - OFFSET_L;
                output = output + ch + (num + "");
            }

            // uppercase letters
            else if(ch >= 'A' && ch <= 'Z'){

                // add numeric val next to letter in string
                int num = Integer.valueOf(ch) - OFFSET_U;
                output = output + ch + (num + "");
            }

            // not a letter
            else{
                output = output + ch;
            }
        }

        return output;
    }
}