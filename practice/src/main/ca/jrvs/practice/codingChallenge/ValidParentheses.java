package ca.jrvs.practice.codingChallenge;

import java.util.HashMap;
import java.util.Stack;

/**
 * ticket: https://www.notion.so/jarvisdev/Valid-Parentheses-4b91cccadb6f4c6ca322618594d44e6b
 *
 * description: Given a string s containing just the characters
 *              '(', ')', '{', '}', '[' and ']', determine if the input string
 *              is valid.
 *
 *              An input string is valid if:
 *
 *                  1. Open brackets must be closed by the same type of brackets.
 *                  2. Open brackets must be closed in the correct order.
 * */

public class ValidParentheses {

    // TODO: time complexity / justification

    /**
     * Big-O: O(n)
     * Justification:
     */

    // main
    public static void main(String[] args) {

        // testing
        String testStr1 = "()[]{}";
        String testStr2 = "{[}]";
        System.out.println(testStr1 + " = " + isValid(testStr1));
        System.out.println(testStr2 + " = " + isValid(testStr2));
    }

    public static boolean isValid(String s) {

        HashMap<Character, Integer> map = genHash(s);
        // System.out.println(map.toString());

        // incorrect matching count of brackets check with map
        if(map.get('(') != map.get(')') ||
            map.get('{') != map.get('}') ||
                map.get('[') != map.get(']')){
            return false;
        }

        // incorrect nesting of brackets check with stack
        Stack<Character> stack = new Stack();
        for(char ch : s.toCharArray()){
            if(ch == '(' || ch == '{' || ch == '['){
                stack.push(ch);
            }

            else if (ch == ')' && !stack.isEmpty() && stack.peek() == '(') {
                stack.pop();
            }

            else if (ch == '}' && !stack.isEmpty() && stack.peek() == '{') {
                stack.pop();
            }

            else if (ch == ']' && !stack.isEmpty() && stack.peek() == '[') {
                stack.pop();
            }

            else {
                return false;
            }
        }

        // System.out.println(stack);

        // stack not empty after operations
        if(!stack.isEmpty()){
            return false;
        }

        return true;
    }

    public static HashMap<Character, Integer> genHash(String s){
        HashMap<Character, Integer> map = new HashMap<>();

        // add character as key to the map
        for(char ch : s.toCharArray()){
            if(!map.containsKey(ch)){
                map.put(ch, 1);
            }

            // key exists? add to the character count
            else{
                map.put(ch, map.get(ch) + 1);
            }
        }

        return map;
    }
}
