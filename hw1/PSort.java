
package hw1;
import java.util.*;
import java.util.concurrent.*;

public class PSort extends RecursiveTask<int[]>{

	int[] arr;
	int beg;
	int end;
	public PSort(int[] a,int beg,int end) {
		this.arr = a;
		this.beg = beg;
		this.end = end;
		
	}
	public int[] sequentialSort(int[] A,int begin,int end) {
		//Arrays.sort(A); //USE THIS FOR TESTING
		//insertion sort
		
		for(int i=0;i<arr.length;i++) {
			int min = arr[i];
			int j = i-1;
			while (j != -1 && arr[j] > min) {
				arr[j+1] = arr[j];
				j--;
			}
			arr[j+1] = min;
		}
		
		return arr;
		
	}
	
	public static void parallelSort(int[] A,int begin,int end) {
		int numProcessors = Runtime.getRuntime().availableProcessors();
		System.out.println("Num processors: ");
		PSort newPSort = new PSort(A,begin,end-1);
		int [] sortedArray;
		if (A.length>16) {
			ForkJoinPool pool = new ForkJoinPool(numProcessors);
			sortedArray = pool.invoke(newPSort);
		}
		else {
			sortedArray = newPSort.sequentialSort(A, begin, end);
		}
		System.out.println("sorted array:");
		System.out.println(Arrays.toString(sortedArray));
		
	}

	@Override
	protected int[] compute() {
		// TODO Auto-generated method stub
		if (this.end <= this.beg) {
			return arr;
		}
		if(this.end - this.beg <= 16) { //if size is less than or equal to 16
			return sequentialSort(arr,beg,end);
		}
		//quicksort algo
		int piv = arr[end]; //pivot
		int lowIndex = beg-1;
		for(int i = beg; i<=end; i++) {
			if(piv > arr[i]) {
				lowIndex++;
				int temp = arr[i];
				arr[i] = arr[lowIndex];
				arr[lowIndex] = temp;
			}
			
		}
		
		lowIndex++;
		int temp = arr[end];
		arr[end] = arr[lowIndex];
		arr[lowIndex] = temp;
		
		
		PSort leftHalf = new PSort(arr,beg,lowIndex-1);
		PSort rightHalf = new PSort(arr,lowIndex+1,end);
		
		leftHalf.fork();
		rightHalf.compute();
		leftHalf.join();
		return arr;
	}


}
