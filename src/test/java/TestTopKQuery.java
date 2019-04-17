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
        int bitSize = 10;
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
        int bitSize = 10;
        int records = 10;
        int arraySize = 15;
        int k = 7;
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

        TopKQuery topKQuery = new TopKQuery(bitSize);
        topKQuery.secureQueryPreCompute(QA,SA,QB,SB);
        topKQuery.genTopKIndexDistTuple(k);

        Pair<BigInteger, BigInteger>[] indexAndDistH = topKQuery.getIndexDistTupleH();
        Pair<BigInteger, BigInteger>[] indexAndDistC = topKQuery.getIndexDistTupleC();

        BigInteger[] reconstructedIndex = new BigInteger[indexAndDistC.length];
        BigInteger[] reconstructedDist = new BigInteger[indexAndDistC.length];

        for (int i = 0; i< indexAndDistC.length;i++){
            reconstructedIndex[i] = reconstruct(indexAndDistC[i].getLeft(), indexAndDistH[i].getLeft(), bitSize);
            reconstructedDist[i] = reconstruct(indexAndDistC[i].getRight(), indexAndDistH[i].getRight(), bitSize);
        }

        System.out.println("Reconstructed Index: ");
        printArr(reconstructedIndex);
        System.out.println("reconstructed Distance: ");
        printArr(reconstructedDist);

        topKQuery.genTopKIndexDistTuple(k);

        Pair<BigInteger, BigInteger>[] kIndexAndDistH = topKQuery.getTopKPairH();
        Pair<BigInteger, BigInteger>[] kIndexAndDistC = topKQuery.getTopKPairC();

        BigInteger[] reconstructedTopKIndex = new BigInteger[kIndexAndDistC.length];
        BigInteger[] reconstructedTopKDist = new BigInteger[kIndexAndDistC.length];

        for (int i = 0; i< kIndexAndDistC.length;i++){
            reconstructedTopKIndex[i] = reconstruct(kIndexAndDistC[i].getLeft(), kIndexAndDistH[i].getLeft(), bitSize);
            reconstructedTopKDist[i] = reconstruct(kIndexAndDistC[i].getRight(), kIndexAndDistH[i].getRight(), bitSize);
        }

        System.out.println("Reconstructed Top k = "+ k + " Index: ");
        printArr(reconstructedTopKIndex);
        System.out.println("reconstructed Distance: ");
        printArr(reconstructedTopKDist);




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

    private void printArr(BigInteger[] arr){

        for(BigInteger bi: arr){
            System.out.print( bi + " ");
        }
        System.out.println();
    }

    private BigInteger reconstruct(BigInteger bigA, BigInteger bigB, int bitSize){
        BigInteger twoToL = BigInteger.TWO.pow(bitSize);
        return bigA.add(bigB).mod(twoToL);
    }
}
