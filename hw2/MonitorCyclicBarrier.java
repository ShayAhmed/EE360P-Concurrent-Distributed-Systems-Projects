/*
 * EID's of group members
 * 
 * ng22457
 * sa43897
 * 
 */
//package hw2;

public class MonitorCyclicBarrier {
	
	int parties;
	int num_left = 0;
	
	public MonitorCyclicBarrier(int parties) {
		
		this.parties = parties;
		num_left = parties;
		
	}
	
	public synchronized int await() throws InterruptedException {
           int index = 0;
           
           num_left--;
           index = num_left;
           
           if(index > 0) {
        	   wait(); // wait until last thread reaches barrier
           } else {
        	   num_left = parties;
        	   notifyAll(); // equivalent to acquiring and releasing semaphores one by one to notify all threads
           }
		
          // you need to write this code
	    return index;
	}
}
