import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;
import secureShuffle.SSF;
import topkQuery.TopKQuery;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

public class TestTopKQuery {
    @Test
    void testSwap(){
        int bitSize = 5;
        TopKQuery topK = new TopKQuery(bitSize);
        BigInteger bigA = BigInteger.valueOf(21);
        BigInteger bigB = BigInteger.valueOf(32);
        BigInteger temp;
        temp = bigA;
        bigA = bigB;
        bigB = temp;
        System.out.println("bigA = " + bigA);
        System.out.println("bigB = " + bigB);
    }

    @Test
    void testSecureQueryPreCompute() throws Exception{
        int bitSize = 5;
        int records = 2;
        int arraySize = 5;
        ArrayList<Triple<BigInteger, BigInteger,BigInteger>> bigTripleQShare = genShares(bitSize, arraySize);
        BigInteger[] QA = new BigInteger[arraySize];
        BigInteger[] QB = new BigInteger[arraySize];
        BigInteger[] Q = new BigInteger[arraySize];

        for(int i=0; i< arraySize;i++ ){
            QA[i]= bigTripleQShare.get(i).getLeft();
            QB[i]= bigTripleQShare.get(i).getRight();
            Q[i]= bigTripleQShare.get(i).getMiddle();
        }

        System.out.print("Original: ");
        printArr(Q);
        System.out.print("QA: ");
        printArr(QA);
        System.out.print("QB: ");
        printArr(QB);

        BigInteger[][] SA = new BigInteger[records][arraySize];
        BigInteger[][] SB = new BigInteger[records][arraySize];
        BigInteger[][] S = new BigInteger[records][arraySize];

        for(int i= 0; i< records; i++){
            ArrayList<Triple<BigInteger, BigInteger,BigInteger>> bigTripleSShare = genShares(bitSize, arraySize);

            for(int j=0; j< arraySize;j++){
                SA[i][j] = bigTripleSShare.get(j).getLeft();
                S[i][j] = bigTripleSShare.get(j).getMiddle();
                SB[i][j] = bigTripleSShare.get(j).getRight();
            }
        }
        System.out.println("S:");
        for(int i= 0; i< records;i++){
            System.out.print("record-"+ i + ": ");
            printArr(S[i]);

        }

        System.out.println("SA:");
        for(int i= 0; i< records;i++){
            System.out.print("record-"+ i + ": ");
            printArr(SA[i]);

        }

        System.out.println("SB:");
        for(int i= 0; i< records;i++){
            System.out.print("record-"+ i + ": ");
            printArr(SB[i]);

        }
        SSF ssf = new SSF(bitSize);
        int[] pi = ssf.getPi(arraySize);
        TopKQuery topKQuery = new TopKQuery(bitSize);
        topKQuery.secureQueryPreCompute(QA,SA,QB,SB, pi);


    }

    private ArrayList<Triple<BigInteger, BigInteger,BigInteger>> genShares(int bitSize, int arraySize) {
        SecureRandom srand = new SecureRandom();
        BigInteger[] arr = new BigInteger[2];
        BigInteger m = BigInteger.TWO.pow(bitSize);

        ArrayList<Triple<BigInteger, BigInteger, BigInteger>> bigTripleArray = new ArrayList<>();
        for(int i=0; i< arraySize; i++){
            BigInteger sum = new BigInteger(bitSize, srand);
            arr[0] = new BigInteger(bitSize, srand);
            arr[1] = (sum.subtract(arr[0])).mod(m);
            bigTripleArray.add(new ImmutableTriple(arr[0], sum, arr[1]));
        }
        return bigTripleArray;
    }

    private static void printArr(BigInteger[] arr){

        for(BigInteger bi: arr){
            System.out.print( bi + " ");
        }
        System.out.println();
    }
}
