package ca.jrvs.practice.codingChallenge;

import java.util.HashSet;

/**
 * ticket: https://www.notion.so/jarvisdev/LinkedList-Cycle-e7be970cefc34058a57e4fd6e60659b0
 *
 * description: Given head, the head of a linked list, determine if the linked
 *              list has a cycle in it. There is a cycle in a linked list if there
 *              is some node in the list that can be reached again by continuously
 *              following the next pointer.
 *
 *              Return true if there is a cycle in the linked list. Otherwise,
 *              return false.
 * */

public class LinkedListCycle {

    // Definition for singly-linked list
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    // TODO: time complexity / justification

    /**
     * Big-O: O(n)
     * Justification:
     */

    // main
    public static void main(String[] args) {

        // testing
        System.out.println("--- LinkedListCycle Class ---");
        ListNode list1 = new ListNode(1);
        list1.next = list1;

        ListNode list2 = new ListNode(1);
        list2.next = new ListNode(2);

        System.out.println(hasCycle(list1));
        System.out.println(hasCycle(list2));
    }
    
    public static boolean hasCycle(ListNode head) {

        HashSet<ListNode> set = new HashSet<ListNode>();

        // iterate through the nodes
        while (head != null) {
            // check if the node is in the hash set, and return true if found
            if (set.contains(head)){
                return true;
            }

            // add the node to the hash set, and update the head pointer
            set.add(head);
            head = head.next;
        }

        return false;
    }
}
