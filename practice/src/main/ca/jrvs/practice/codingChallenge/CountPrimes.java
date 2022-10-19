package ca.jrvs.practice.codingChallenge;

/**
 * ticket: https://www.notion.so/jarvisdev/Count-Primes-8226426bfbbb45f0a4da5ebd366061a9
 *
 * description: Given an integer n, return the number of prime numbers that are strictly less than n.
 * */

public class CountPrimes {

    // TODO: time complexity / justification

    /**
     * Big-O: O(n)
     * Justification:
     */

    // main
    public static void main(String[] args) {

        // testing
        int num = 10;
        System.out.println("There are (" + countPrimes(num) + ") prime numbers less than " + num);
    }

    // helper function - checks if a number is prime
    public static boolean isPrime(int n){

        for(int i = 2; i * i <= n ; i++){
            if(n % i == 0){
                return false;
            }
        }

        return true;
    }

    public static int countPrimes(int n) {

        int primeCount = 0;

        // skip counting initial cases of 0, 1, and 2
        if(n > 2){
            primeCount+=1;
            System.out.println("val: 2 - prime count: 1");
        }

        // skip even numbers after 3
        for(int i = 3; i < n; i+=2){
            if(isPrime(i)){
                primeCount++;
                System.out.println("val: " + i + " - prime count: " + primeCount);
            }
        }

        return primeCount;
    }
}
