package ca.jrvs.practice.codingChallenge;

/**
 * ticket: https://www.notion.so/jarvisdev/Nth-Node-From-End-of-LinkedList-076ebca267b6485c8f26e530ef936895
 *
 * description: Given the head of a linked list, remove the nth node from the
 *              end of the list and return its head.
 * */

public class removeNthNodeFromEndOfList {

    // Definition for singly-linked list
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }

        // print method added to class for testing purposes...
        public void printAll(){
            System.out.print(val + " -> ");
            if (next != null){
                next.printAll();
            }
            if(next == null){
                System.out.println("null");
            }
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
        System.out.println("--- removeNthNode Class ---");
        int delIndex = 4;

        ListNode list = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5,null)))));
        System.out.print("list before removal: "); list.printAll();
        System.out.println("delete [" + delIndex + "]th node from end");
        removeNthFromEnd(list, delIndex);
        System.out.print("list after removal: "); list.printAll();
    }

    public static ListNode removeNthFromEnd(ListNode head, int n) {

        ListNode current = head;

        int size = getSize(head, 0);
        int count = 0;
        int target = size - n;

        // System.out.println("size: " + size);

        // removing the only element
        if(n == 1 && size == 1) return null;

        // removing the end element
        else if (n == 1){
            while(current != null){

                if(count == target - 1){
                    // System.out.println("at: " + current.val);
                    current.val = current.val;
                    current.next = null;
                }

                count++;
                current = current.next;
            }
        }

        // removing any element in the list thats not at the end
        else{
            while(current != null){

                if(count == target){
                    current.val = current.next.val;
                    current.next = current.next.next;
                }

                count++;
                current = current.next;
            }
        }

        return head;
    }

    public static int getSize(ListNode head, int s){

        ListNode temp = head;

        while(temp != null){
            s++;
            temp = temp.next;
        }

        return s;
    }
}
