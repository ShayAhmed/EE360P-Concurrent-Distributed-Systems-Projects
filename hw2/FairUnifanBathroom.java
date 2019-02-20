//package hw2;

/*
 * EID's of group members
 * 
 * ng22457
 * sa43897
 * 
 */
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
public class FairUnifanBathroom {
    int state[];
    int numouwant = 0, numouusing = 0,inRR = 2;
    int numutwant=0, numutusing=0;
    
	ReentrantLock monitorlock = new ReentrantLock();
	Condition UT = monitorlock.newCondition();
	Condition OU = monitorlock.newCondition();

	int resources = 5;
	
	ArrayList<Integer> bathroomline = new ArrayList<Integer>();
	
	public boolean checkcondit(int UTorOU,int UTorOUusing) {
		//System.out.println(resources+"resources");
		int inbathroom = -1;
		if(bathroomline.size() !=0) {
			inbathroom = bathroomline.get(0);
			
		}
		
		if (resources == 0|| UTorOUusing >0||inbathroom == (1 - UTorOU)) { //if bathroom is full, or other team is in bathroom, or next person in line is from other team
			return true;
		}

		return false;
	}
    
	//UT ==0;
	//OU ==1;
	public void enterBathGeneral(int UTorOU) {
		return;
    	
    	
    	
	}
	
    public void enterBathroomUT() {
    	
    	monitorlock.lock();
    	if(checkcondit(0,numouusing) == true) {//if bathroom is full, or other team is in bathroom, or next person in line is from other team
    		numutwant++;
    		bathroomline.add(0);
    	}
    	
    	while(checkcondit(0,numouusing)==true) {
    		try {
				UT.await();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//System.out.println(e);
			}
//    	numutwant++;
//    	bathroomline.add(0);
    		
    	}
    	if(numutwant != 0) {
    		numutwant--;
    		bathroomline.remove(0);
    	}
    	resources--;
    	numutusing++;
    	monitorlock.unlock();
    	
    	
    	
    }
    public void enterBathroomOU() {
    	monitorlock.lock();
    	if(checkcondit(1,numutusing) == true) {
    		numouwant++;
    		bathroomline.add(1);
    	}
    	
    	while(checkcondit(1,numutusing)==true) {
    		try {
				OU.await();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//System.out.println(e);
			}
    		
    	}
    	if(numouwant != 0) {
    		numouwant--;
    		bathroomline.remove(0);
    	}
    	resources--;
    	numouusing++;
    	monitorlock.unlock();
    }
    public void leaveBathroomGeneral(int UTorOU) {return;}
    
    public void leaveBathroomUT() {
    	monitorlock.lock();
    	numutusing--;
    	resources++;
    	
    	int nextinline = 0;
    	if(bathroomline.size() !=0) {
    		nextinline = bathroomline.get(0);
    	}else {
    		nextinline = -1;
    	}
    	
    	if(nextinline== -1) {
//    		monitorlock.unlock();
//    	
//    		return;
    	}
    	 
    	if(nextinline ==0) {
			UT.signalAll();
			OU.signalAll();
		}else{
			UT.signalAll();
			OU.signalAll();
			
	}
//    	numutusing--;
//    	resources++;
//    	
//    	UT.signalAll();
//    	OU.signalAll();
    	monitorlock.unlock();
    	
    }
    public void leaveBathroomOU() {
    	monitorlock.lock();
    	numouusing--;
    	resources++;
    	int nextinline = 0;
    	if(bathroomline.size() !=0) {
    		nextinline = bathroomline.get(0);
    	}
    	else {
    		nextinline = -1;
    	}
    	if(nextinline== -1) {
//    		monitorlock.unlock();
//    		return;
    	}	
    	
    	if(nextinline ==0) {
    			UT.signalAll();
    			OU.signalAll();
    		}else{
    			UT.signalAll();
    			OU.signalAll();
    			
    	}
    	
//    	numouusing--;
//    	resources++;
//    	UT.signalAll();
//    	OU.signalAll();
    	monitorlock.unlock();
    }
}