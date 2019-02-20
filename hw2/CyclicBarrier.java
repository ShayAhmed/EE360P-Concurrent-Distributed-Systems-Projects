/*
 * EID's of group members

 * 
 * ng22457
 * sa43897
 * 
 */
//package hw2;

import java.util.concurrent.Semaphore; // for implementation using Semaphores

public class CyclicBarrier {
	
	int parties;
	int num_left = 0;
	Semaphore receivingBarrier = new Semaphore(0);
	Semaphore resumingBarrier = new Semaphore(1);
	Semaphore mutex = new Semaphore(1);
	
	public CyclicBarrier(int parties) {
		
		this.parties = parties;
		num_left = parties;
		
	}
	
	public int await() throws InterruptedException {
           int index = 0;
           
           mutex.acquire();
           
           num_left--;
           index = num_left; // setting the proper index value
           
           if(index == 0) {
        	   resumingBarrier.acquire();
        	   receivingBarrier.release(); // if its the last thread, release first semaphore
           }
           
           mutex.release();
           
           receivingBarrier.acquire(); // all threads block here until last thread releases first semaphore
           receivingBarrier.release(); // lets all blocked threads move on one by one (analogous to notifyAll) 
           
           
           mutex.acquire();
           
           num_left++;
           
           if(num_left == parties) {
        	   
        	   receivingBarrier.acquire(); // if its the last thread, acquire first semaphore to block ensuing threads at line 37
        	   resumingBarrier.release(); // release all threads that are blocked at line 54
        	   
           }
           
           mutex.release();
           
           resumingBarrier.acquire(); // all threads are blocked here until last thread releases second semaphore
           resumingBarrier.release(); // lets all blocked threads move on one by one (analogous to notifyAll)
		
          // you need to write this code
	    return index;
	}
}
