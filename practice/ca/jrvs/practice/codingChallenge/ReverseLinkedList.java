package ca.jrvs.practice.codingChallenge;

/**
 * ticket: https://www.notion.so/Reverse-Linked-List-3111660b5d8c4af08ffb52be1d28d3ff
 * description:
 * Given the head of a singly linked list, reverse the list, and return the reversed list.
 */

public class ReverseLinkedList {

    /**
     * Big-O:
     * Justification:
     */

    // ListNode structure
    public static class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }

        public void printAll(){
            System.out.print(val + " -> ");
            if (next != null){
                next.printAll();
            }
            if(next == null){
                System.out.println("");
            }
        }
  }

    // main
    public static void main(String[] args) {

        // testing
        System.out.println("--- ReverseLinkedList Class ---");
        ListNode list = new ListNode(21, new ListNode(5, new ListNode(19, new ListNode(8, null))));
        list.printAll();
        reverseListI(list).printAll();
    }

    // iterative approach to reversing a linked list
    public static ListNode reverseListI(ListNode listHeadPtr) {

        ListNode prev = null;
        ListNode current = listHeadPtr;
        ListNode next;

        // walk through the linked list pointer until null pointer reached
        while(current != null){

            // next from current head becomes the new head pointer, and moved into previous
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }

        // new head pointer is the previous values before the head, after the while block finishes
        listHeadPtr = prev;
        return listHeadPtr;
    }

    // TODO: recursive list reverse...
    public static ListNode reverseListR(ListNode listHeadPtr) {

        return listHeadPtr;

    }
}
