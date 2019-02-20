package hw1;
import java.util.Arrays;

public class MergeTest {
  public static void main (String[] args) {
	  
    //int[] A1 = {1, 3, 5, 7, 9};
   //int[] B1 = {2, 4, 6, 8, 10};
    
    int[] A2 = {13, 60, 1000, 3000, 129948};
    int[] B2 = {1, 2, 3, 5, 10,11,12,13,14,15,16,17,18,19};
    int[] C1 = new int[A2.length+B2.length];
    PMerge.parallelMerge(A2, B2, C1, 7);
    for(int i=0;i<C1.length;i++) {
    	System.out.println(C1[i]);
    }
    
    
    
    
    
    
    
    
    //verifyParallelMerge(A1, B1);
    System.out.println("here1");
    
    verifyParallelMerge(A2, B2);

    int[] A3 = {49, 75, 174, 365, 374, 408, 411, 455, 511, 749, 814, 1012, 1200, 1224, 1280, 1447, 1520, 1545, 1815, 1821, 1922, 2010, 2027, 2266, 2366, 2531, 2557, 2803, 2933, 2979, 3099, 3185, 3189, 3216, 3247, 3296, 3319, 3320, 3647, 3680, 3735, 3736, 3771, 3795, 3860, 3884, 3933, 3980, 4125, 4279, 4392, 4476, 4606, 4664, 4689, 4699, 4768, 4786, 5062, 5103, 5388, 5495, 5580, 5702, 5808, 6281, 6315, 6547, 6657, 6733, 6814, 6890, 6893, 6988, 6991, 7497, 7706, 7891, 7906, 7930, 7989, 8038, 8058, 8097, 8234, 8354, 8387, 8449, 8463, 8606, 8614, 8887, 8975, 9249, 9268, 9270, 9560, 9639, 9905, 9963};
    int[] B3 = {109, 206, 318, 329, 345, 364, 449, 475, 503, 582, 967, 1027, 1121, 1145, 1193, 1415, 1471, 1599, 1768, 1797, 1868, 1874, 1994, 2125, 2256, 2488, 2583, 2648, 2751, 2820, 2830, 2834, 2842, 2860, 2967, 2997, 3230, 3290, 3837, 3955, 3970, 4015, 4020, 4114, 4250, 4345, 4665, 4811, 4857, 5153, 5154, 5219, 5366, 5606, 5720, 5744, 5750, 5784, 5938, 6084, 6261, 6346, 6521, 6606, 6657, 6750, 6829, 6984, 7070, 7121, 7192, 7324, 7417, 7579, 7604, 7677, 7821, 7868, 8013, 8036, 8066, 8203, 8426, 8689, 8783, 8828, 8840, 8909, 8935, 8940, 8949, 9133, 9305, 9375, 9514, 9756, 9805, 9844, 9923, 9988};
    verifyParallelMerge(A3, B3);

    int[] A4 = {1, 3, 6, 7, 8, 10};
    int[] B4 = {1, 3, 6, 7, 8, 10};
    verifyParallelMerge(A4, B4);
  }

  static void verifyParallelMerge(int[] A, int[] B) {
    int[] C = new int[A.length + B.length];
	int[] D = new int[A.length + B.length];

    System.out.println("Verify Parallel Merge for arrays: ");
    printArray(A);

	printArray(B);
    merge(A, B, C);

    PMerge.parallelMerge(A, B, D, 10);
   	
    boolean isSuccess = true;
    for (int i = 0; i < C.length; i++) {
      if (C[i] != D[i]) {
        System.out.println("Your parallel sorting algorithm is not correct");
        System.out.println("Expect:");
        printArray(C);
        System.out.println("Your results:");
        printArray(D);
        isSuccess = false;
        break;
      }
    }

    if (isSuccess) {
      System.out.println("Great, your sorting algorithm works for this test case");
    }
    System.out.println("=========================================================");
  }

  public static void merge(int[] A, int[] B, int[] C) {
  	int h = 0, i = 0, j = 0;
	while(i < A.length || j < B.length) {
		if(i == A.length) {
			C[h ++] = B[j ++];
		} else if(j == B.length) {
			C[h ++] = A[i ++];
		} else {
			if(A[i] < B[j]) C[h ++] = A[i ++];
			else C[h ++] = B[j ++];
		}
	}
  }

  public static void printArray(int[] A) {
    for (int i = 0; i < A.length; i++) {
      if (i != A.length - 1) {
        System.out.print(A[i] + " ");
      } else {
        System.out.print(A[i]);
      }
    }
    System.out.println();
  }
}