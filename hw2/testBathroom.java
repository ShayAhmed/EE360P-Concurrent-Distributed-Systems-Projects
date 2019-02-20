package hw2;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class testBathroom implements Runnable {
	final FairUnifanBathroom bthrm;
	public testBathroom (FairUnifanBathroom f) {
		this.bthrm = f;
		
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub

		int sleepTime = 5;
		if(Thread.currentThread().getId()%2 == 0) {
			System.out.println("OU Thread "+ Thread.currentThread().getId() + " running" );
			bthrm.enterBathroomOU();
			System.out.println("OU Thread " + Thread.currentThread().getId() + " enters bathroom");
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bthrm.leaveBathroomOU();
			System.out.println("OU Thread " + Thread.currentThread().getId() + " leaves bathroom");
			
			
		}
		else {
			System.out.println("UT Thread "+ Thread.currentThread().getId() + " running");
			bthrm.enterBathroomUT();
			System.out.println("UT Thread " + Thread.currentThread().getId() + " enters bathroom");
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bthrm.leaveBathroomUT();
			System.out.println("UT Thread " + Thread.currentThread().getId() + " leaves bathroom");
				
		}
		
	}
	public static void main(String[] args) {
		
		FairUnifanBathroom test = new FairUnifanBathroom();
		Thread[] t = new Thread[30];
		for(int i = 0; i < 30; i++) {
			Thread myThread = new Thread(new testBathroom(test));
			
			 t[i] = myThread;
			 
		}
		for (int i = 0; i < 30; ++i) {

			t[i].start();
		}
	}

}
