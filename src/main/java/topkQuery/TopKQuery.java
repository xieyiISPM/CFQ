package topkQuery;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import secureExactEditDistance.SecureExactEditDistance;
import secureShuffle.OfflineShuffling;
import secureShuffle.SSF;

import java.math.BigInteger;

public class TopKQuery {
    private int bitSize;
    private BigInteger twoToL;

    private Pair<BigInteger, BigInteger> [] indexDistTupleH;
    private Pair<BigInteger, BigInteger>[] indexDistTupleC;
    private int m;
    private int n;

    private BigInteger[][] deltaC;
    private BigInteger[][] deltaH;

    private Pair<BigInteger, BigInteger>[] kIndexDistTupleH;
    private Pair<BigInteger, BigInteger>[] kIndexDistTupleC;

    public TopKQuery(int bitSize){
        this.bitSize = bitSize;
        this.twoToL = BigInteger.TWO.pow(bitSize);
    }

    public void secureQueryPreCompute(BigInteger[] QA, BigInteger[][] SA, BigInteger[] QB, BigInteger[][] SB, int[] pi) throws Exception{
        this.m = SA.length; //row length
        this.n = SA[0].length; //column length

        BigInteger[] dEDA;
        BigInteger[] dEDB;

        dEDA= new BigInteger[m];
        dEDB = new BigInteger[m];
        indexDistTupleH = new Pair[m];
        indexDistTupleC = new Pair[m];
        SecureExactEditDistance seed = new SecureExactEditDistance(bitSize);
        for (int i = 0; i< m; i++){
            seed.setDistance(SA[i],SB[i],QA,QB);
            dEDA[i]= seed.getDedA();
            dEDB[i] = seed.getDedB();
        }

        BigInteger[] indexA  = new BigInteger[m];
        BigInteger[] indexB  = new BigInteger[m];
        for (int i= 0; i< m; i++){
            indexA[i] = BigInteger.valueOf(i).mod(twoToL);
            indexB[i] = BigInteger.valueOf(i).mod(twoToL);
        }

        int arraySize = n;
        SSF ssf = new SSF(bitSize);
        OfflineShuffling offlineShufflingIndex = new OfflineShuffling();
        OfflineShuffling offlineShufflingDist = new OfflineShuffling();

        BigInteger[] indexHPrime = ssf.getOfflineOutput(arraySize, offlineShufflingIndex, pi);
        BigInteger[] distHPrime = ssf.getOfflineOutput(arraySize,offlineShufflingDist,pi);

        BigInteger[] indexCPrime = ssf.getOnlineOuptut(arraySize,indexA, indexB, offlineShufflingIndex, pi);
        BigInteger[] distCPrime = ssf.getOnlineOuptut(arraySize, dEDA,dEDB,offlineShufflingDist,pi);


        for(int i = 0; i< m; i++){
            indexDistTupleH[i]= new ImmutablePair(indexHPrime[i], distHPrime[i]);
            indexDistTupleC[i]= new ImmutablePair(indexCPrime[i],distCPrime[i]);
        }
    }

    public void genTopKIndexDistTuple(int k){
        deltaC = new BigInteger[m][2];
        deltaH = new BigInteger[m][2];

        for(int i= 0; i< m; i++){
            deltaC[i][0]= indexDistTupleC[i].getLeft();
            deltaH[i][0]= indexDistTupleH[i].getLeft();
            deltaC[i][1]= indexDistTupleC[i].getRight();
            deltaH[i][1]= indexDistTupleH[i].getRight();
        }

        for(int i= 0; i< k;i++){
            for(int j= m; j>= i; j--){
                int theta = thetaHelper(deltaC[j][1], deltaH[j][1], deltaC[j-1][1], deltaH[j-1][1]);
                if(theta==0) {
                    /**
                     * Java pass by value, therefore, I can not create a method to do swap
                     */
                   BigInteger temp;
                   temp = deltaC[j][0];
                   deltaC[j-1][0] = deltaC[j][0];
                   deltaC[j][0]= temp;

                   temp = deltaH[j][0];
                   deltaH[j-1][0] = deltaH[j][0];
                   deltaH[j][0]= temp;

                    temp = deltaC[j][1];
                    deltaC[j-1][1] = deltaC[j][1];
                    deltaC[j][1]= temp;

                    temp = deltaH[j][1];
                    deltaH[j-1][1] = deltaH[j][1];
                    deltaH[j][1]= temp;
                }

            }
        }

        for (int i = 0; i < k; i++){
            kIndexDistTupleH[i]= new ImmutablePair(deltaH[i][0], deltaH[i][1]);
            kIndexDistTupleC[i]= new ImmutablePair(deltaC[i][0],deltaC[i][1]);
        }
    }



    public Pair<BigInteger, BigInteger>[] getIndexDistTupleH(){
        return indexDistTupleH;
    }

    public Pair<BigInteger, BigInteger>[] getIndexDistTupleC(){
        return indexDistTupleC;
    }

    public Pair<BigInteger, BigInteger>[] getTopKPairH(){
        return kIndexDistTupleH;
    }

    public Pair<BigInteger, BigInteger>[] getTopKPairC(){
        return kIndexDistTupleC;
    }


    private int thetaHelper(BigInteger xA, BigInteger xB, BigInteger yA, BigInteger yB){
        int result = (xA.add(xB).mod((BigInteger.TWO).pow(bitSize))).compareTo(yA.add(yB).mod((BigInteger.TWO).pow(bitSize)));
        if(result >0){
            return 1;
        }
        else return 0;
    }




}
