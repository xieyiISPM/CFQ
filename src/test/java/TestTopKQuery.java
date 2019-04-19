import helper.Helper;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import topkQuery.TopKQuery;

import java.math.BigInteger;

public class TestTopKQuery {
    @Test
    void testSwap(){
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
        int arraySize = 6;
        int k = 5;

        Helper helper = new Helper(bitSize);
        helper.genQuery(arraySize);
        BigInteger[] QA = helper.getQueryA();
        BigInteger[] QB = helper.getQueryB();
        BigInteger[] Q = helper.getOriginalQuery();


        System.out.print("Original: ");
        helper.printArr(Q);
        System.out.print("QA: ");
        helper.printArr(QA);
        System.out.print("QB: ");
        helper.printArr(QB);

        helper.genGS(arraySize, records);
        BigInteger[][] SA = helper.getGSA();
        BigInteger[][] SB = helper.getGSB();
        BigInteger[][] S = helper.getOriginalGS();

        System.out.println("S:");
        for(int i= 0; i< records;i++){
            System.out.print("record-"+ i + ": ");
            helper.printArr(S[i]);

        }

        System.out.println("SA:");
        for(int i= 0; i< records;i++){
            System.out.print("record-"+ i + ": ");
            helper.printArr(SA[i]);

        }

        System.out.println("SB:");
        for(int i= 0; i< records;i++){
            System.out.print("record-"+ i + ": ");
            helper.printArr(SB[i]);

        }

        TopKQuery topKQuery = new TopKQuery(bitSize);
        topKQuery.secureQueryPreCompute(QA,SA,QB,SB);
        topKQuery.genTopKIndexDistTuple(k);

        Pair<BigInteger, BigInteger>[] indexAndDistH = topKQuery.getIndexDistTupleH();
        Pair<BigInteger, BigInteger>[] indexAndDistC = topKQuery.getIndexDistTupleC();

        BigInteger[] reconstructedIndex = new BigInteger[indexAndDistC.length];
        BigInteger[] reconstructedDist = new BigInteger[indexAndDistC.length];

        for (int i = 0; i< indexAndDistC.length;i++){
            reconstructedIndex[i] = helper.reconstruct(indexAndDistC[i].getLeft(), indexAndDistH[i].getLeft(), bitSize);
            reconstructedDist[i] = helper.reconstruct(indexAndDistC[i].getRight(), indexAndDistH[i].getRight(), bitSize);
        }

        System.out.println("Reconstructed Index: ");
        helper.printArr(reconstructedIndex);
        System.out.println("reconstructed Distance: ");
        helper.printArr(reconstructedDist);

        topKQuery.genTopKIndexDistTuple(k);

        Pair<BigInteger, BigInteger>[] kIndexAndDistH = topKQuery.getTopKPairH();
        Pair<BigInteger, BigInteger>[] kIndexAndDistC = topKQuery.getTopKPairC();

        BigInteger[] reconstructedTopKIndex = new BigInteger[kIndexAndDistC.length];
        BigInteger[] reconstructedTopKDist = new BigInteger[kIndexAndDistC.length];

        for (int i = 0; i< kIndexAndDistC.length;i++){
            reconstructedTopKIndex[i] = helper.reconstruct(kIndexAndDistC[i].getLeft(), kIndexAndDistH[i].getLeft(), bitSize);
            reconstructedTopKDist[i] = helper.reconstruct(kIndexAndDistC[i].getRight(), kIndexAndDistH[i].getRight(), bitSize);
        }

        System.out.println("Reconstructed Top k = "+ k + " Index: ");
        helper.printArr(reconstructedTopKIndex);
        System.out.println("reconstructed Distance: ");
        helper.printArr(reconstructedTopKDist);

    }


    /*private void printArr(BigInteger[] arr){

        for(BigInteger bi: arr){
            System.out.print( bi + " ");
        }
        System.out.println();
    }

    private BigInteger reconstruct(BigInteger bigA, BigInteger bigB, int bitSize){
        BigInteger twoToL = BigInteger.TWO.pow(bitSize);
        return bigA.add(bigB).mod(twoToL);
    }*/
}
