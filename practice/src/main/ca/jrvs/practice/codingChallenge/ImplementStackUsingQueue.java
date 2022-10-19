package ca.jrvs.practice.codingChallenge;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * ticket: https://www.notion.so/jarvisdev/Implement-Stack-using-Queue-2b5637e90e55469687831297176d6e73
 *
 * description: Implement a last-in-first-out (LIFO) stack using only two
 *              queues. The implemented stack should support all the functions
 *              of a normal stack (push, top, pop, and empty).
 *
 *              Implement the MyStack class:
 *
 *                  void push(int x) Pushes element x to the top of the stack.
 *                  int pop() Removes the element on the top of the stack and returns it.
 *                  int top() Returns the element on the top of the stack.
 *                  boolean empty() Returns true if the stack is empty, false otherwise.
 * */

public class ImplementStackUsingQueue {

    // TODO: time complexity / justification

    /**
     * Big-O: O(n)
     * Justification:
     */

    // main
    public static void main(String[] args) {

        // testing
        System.out.println("--- ImplementStackUsingQueue Class ---");
        MyStack obj = new MyStack();
        obj.push(1);
        obj.push(2);
        obj.push(3);

        System.out.println("top value = " + obj.top());
        System.out.println("popped value = " + obj.pop());
        obj.push(4);
        System.out.println("empty? " + obj.empty());
    }

    public static class MyStack {

        Queue<Integer> queue;

        public MyStack() {
            queue = new LinkedList<>();
        }

        public void push(int x) {
            queue.add(x);
        }

        public int pop() {

            System.out.println("stack before pop: " + queue.toString());
            List<Integer> temp = new LinkedList<>();
            int retVal = -1; // temp val

            // add all values in the queue into a list
            for(int val : queue){
                temp.add(val);
            }

            // empty the queue
            while(!queue.isEmpty()){
                queue.remove();
            }

            // repopulate the queue minus the stack head, and update the popped value
            for(int i = 0; i < temp.size(); i++){
                if(i == temp.size()-1){
                    retVal = temp.get(i);
                    // System.out.println("ret pop: " + temp.get(i));
                }
                else{
                    // System.out.println(temp.get(i));
                    queue.add(temp.get(i));
                }
            }
            System.out.println("stack after pop: " + queue.toString());
            return retVal;
        }

        public int top() {

            System.out.println("stack before top: " + queue.toString());
            List<Integer> temp = new LinkedList<>();
            int retVal = -1; // temp val

            // add all values in the queue into a list
            for(int val : queue){
                temp.add(val);
            }

            // update the top value
            for(int i = 0; i < temp.size(); i++){
                if(i == temp.size()-1){
                    retVal = temp.get(i);
                    // System.out.println("ret top: " + temp.get(i));
                }
            }

            return retVal;
        }

        public boolean empty() {
            System.out.println("stack current: " + queue.toString());
            return queue.isEmpty();
        }
    }
}
