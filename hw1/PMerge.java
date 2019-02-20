package hw1;
import java.util.*;
import java.util.concurrent.*;


public class PMerge {
	
	int[] a;
	int[] b;
	int[] c;
	public static ExecutorService es;
	int task_num = 0;
	static int[] B_ranks;
	
	public PMerge(int[] A, int[] B, int[]C, int numThreads) {
		this.a = A;
		this.b = B;
		this.c = C;
		es = Executors.newFixedThreadPool(numThreads);
		B_ranks = new int[numThreads-1];
	}
	
	public static void parallelMerge(int[] A, int[] B, int[]C, int numThreads){
    // TODO: Implement your parallel merge function
		
		PMerge merge = new PMerge(A,B,C,numThreads);
		int[][] subArrays = divide(A,numThreads-1);
		List<Future> ranks = new ArrayList<Future>();
		
		for(int i=0;i<numThreads-1;i++) {
			SubArray curr = new SubArray(subArrays[i],B);
			Future<Integer> f = es.submit(curr);
			ranks.add(i, f);
		}
		
		for(int i=0;i<numThreads-1;i++) {
			try {
				B_ranks[i] = (int) ranks.get(i).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ranks.clear();
		
		for(int i=0;i<numThreads-1;i++) {
			mergeArray curr = new mergeArray(subArrays[i],B_ranks,B);
			ranks.add(es.submit(curr));
		}
		
		try {			
			int[][] sortedSubArrays = new int[numThreads-1][];
			
			for(int i=0;i<numThreads-1;i++) {
				sortedSubArrays[i] = (int[]) ranks.get(i).get();
			}
			
			int final_pos = 0;
			for(int i=0;i<numThreads-1;i++) {
				System.arraycopy(sortedSubArrays[i], 0, merge.c, final_pos, sortedSubArrays[i].length);
				final_pos += sortedSubArrays[i].length;
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		es.shutdown();
		try {
			es.awaitTermination(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static int[][] divide(int[] A, int n){
		
		int subSize = (int) Math.ceil((double)A.length/n);
		
		int[][] sub = new int[n][];

		int sub_start = 0;
		for(int i=0;i<n;++i) {
			sub_start = i * subSize;
			
			int length = Math.min(subSize, A.length - sub_start);
			sub[i] = new int[length];
			System.arraycopy(A, sub_start, sub[i], 0, length);
		}
		
		return sub;
	}
	
	public static void main(String[] args) {
		
		int[] A = {1,2,5,7,9,11,15,16};
		int[] B = {3,4,8,13,16};
		int[] C = new int[13];

		parallelMerge(A,B,C,3);
		
		for(int i=0;i<C.length;i++) {
			System.out.print(C[i] + " ");
		}
	}
}

class SubArray implements Callable<Integer>{
	
	int[] subA;
	int[] B;
	
	public SubArray(int[] subA, int[] B) {
		
		this.subA = subA;
		this.B = B;
	}

	@Override
	public Integer call() throws Exception {
		// TODO Auto-generated method stub
		
		int last_elem = subA[subA.length-1];
		
		int rank = Arrays.binarySearch(B, last_elem);
		
		if(rank < 0) {
			rank = -rank;
			rank = rank - 2;
		}
		
		return rank;
	}
	
}

class mergeArray implements Callable<int[]>{
	
	int[] subA;
	int[] B_ranks;
	int[] B;
	
	public mergeArray(int[] subA, int[] B_ranks, int[] B) {
		
		this.subA = subA;
		this.B_ranks = B_ranks;
		this.B = B;
		
	}

	@Override
	public int[] call() throws Exception {
		// TODO Auto-generated method stub
		
		int A_i = 0;
		int B_i = 0;
		
		int last_elem = subA[subA.length-1];
		
		int curr_rank = 0;
		int rank_index = 0;
		int subB_length;
		
		for(int i=0;i<B_ranks.length;i++) {
			if(B[B_ranks[i]] <= last_elem) {
				curr_rank = B_ranks[i];
				rank_index = i;
			}else {
				break;
			}
		}
		
		if(rank_index == 0) {
			subB_length = curr_rank+1;
			B_i = 0;
		} else {
			subB_length = curr_rank - B_ranks[rank_index-1];
			B_i = B_ranks[rank_index-1]+1;
		}
		
		int[] result = new int[subA.length+subB_length];
		int k = 0;
		int initial_b_index = B_i;
		
		while(A_i < subA.length && B_i < (initial_b_index+subB_length)) {
			
			if(subA[A_i] <= B[B_i]) {
				result[k] = subA[A_i];
				A_i++;
			} else if(subA[A_i] > B[B_i]) {
				result[k] = B[B_i];
				B_i++;
			}
			
			k++;
		}
		
		while(A_i < subA.length) {
			
			result[k] = subA[A_i];
			A_i++;
			k++;
			
		}
		
		while(B_i < initial_b_index + subB_length) {
			
			result[k] = B[B_i];
			B_i++;
			k++;
			
		}
		
		return result;
				
	}

	
}