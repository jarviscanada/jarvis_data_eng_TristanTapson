package ca.jrvs.practice.codingChallenge;

public class MiddleOfTheLinkedList {

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

    // main
    public static void main(String[] args) {

        // testing
        System.out.println("--- MiddleOfLinkedList Class ---");

        ListNode list = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5,null)))));
        System.out.print("list before middle partition: "); list.printAll();
        System.out.print("list after middle partition: "); middleNode(list).printAll();
    }

    public static ListNode middleNode(ListNode head) {

        int size = getSize(head, 0);
        int count = 0;
        int half;

        // odd-even halfway value
        half = (size / 2);

        // iterate through the nodes until the halfway point
        while(count < half){
            count++;
            head = head.next;
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
