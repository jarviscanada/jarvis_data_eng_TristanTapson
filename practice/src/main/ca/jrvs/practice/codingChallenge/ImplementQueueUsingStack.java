package ca.jrvs.practice.codingChallenge;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * ticket: https://www.notion.so/jarvisdev/Implement-Queue-using-Stacks-34bfa7f2b07240cc88f1643cfae0e427
 *
 * description: Implement a first in first out (FIFO) queue using only two stacks.
 *              The implemented queue should support all the functions of a
 *              normal queue (push, peek, pop, and empty).
 *
 *              Implement the MyQueue class:
 *
 *                  void push(int x) Pushes element x to the back of the queue.
 *                  int pop() Removes the element from the front of the queue and returns it.
 *                  int peek() Returns the element at the front of the queue.
 *                  boolean empty() Returns true if the queue is empty, false otherwise.
 * */

public class ImplementQueueUsingStack {

    // TODO: time complexity / justification

    /**
     * Big-O: O(n)
     * Justification:
     */

    // main
    public static void main(String[] args) {

        // testing
        System.out.println("--- ImplementQueueUsingStack Class ---");
        MyQueue obj = new MyQueue();
        obj.push(1);
        obj.push(2);
        obj.push(3);

        System.out.println("peeked value = " + obj.peek());
        System.out.println("popped value = " + obj.pop());
        obj.push(4);
        System.out.println("empty? " + obj.empty());
    }

    public static class MyQueue {

        Stack<Integer> stack;

        public MyQueue() {
            stack = new Stack<>();
        }

        public void push(int x) {
            stack.push(x);
        }

        public int pop() {

            System.out.println("queue before pop: " + stack.toString());
            List<Integer> temp = new LinkedList<>();
            int retVal = -1; // temp val

            // add all values in the stack into a list
            for(int val : stack){
                temp.add(val);
            }

            // empty the stack
            while(!stack.empty()){
                stack.pop();
            }

            // repopulate the stack minus the queue head, and update the popped value
            for(int i = 0; i < temp.size(); i++){
                if(i == 0){
                    retVal = temp.get(i);
                    // System.out.println("ret pop: " + temp.get(i));
                }
                else{
                    // System.out.println(temp.get(i));
                    stack.push(temp.get(i));
                }
            }
            System.out.println("queue after pop: " + stack.toString());
            return retVal;
        }

        public int peek() {

            System.out.println("queue before peek: " + stack.toString());
            List<Integer> temp = new LinkedList<>();
            int retVal = -1;

            // add all values in the stack into a list
            for(int val : stack){
                temp.add(val);
            }

            // update the peek value
            for(int i = 0; i < temp.size(); i++){
                if(i == 0){
                    retVal = temp.get(i);
                    // System.out.println("ret peek: " + temp.get(i));
                    break;
                }
            }

            return retVal;
        }

        public boolean empty() {
            System.out.println("queue current: " + stack.toString());
            return stack.empty();
        }
    }
}
