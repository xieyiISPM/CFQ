package topkQuery;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import secureExactEditDistance.SecureExactEditDistance;
import secureShuffle.OfflineShuffling;
import secureShuffle.OfflineShufflingPool;
import secureShuffle.SSF;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Generate top k-NN of one Hospital
 * I assume Hospital is B, and Cloud is A
 * In this Class, we first compute all (index,distance) pair and sort all by distance
 */
public class TopKQuery {
    private int bitSize;
    private BigInteger twoToL;

    private Pair<BigInteger, BigInteger> [] indexDistTupleB;
    private Pair<BigInteger, BigInteger>[] indexDistTupleA;
    private int m; //sequence length
    private int n; // records number

    private BigInteger[][] deltaA;
    private BigInteger[][] deltaB;

    private Pair<BigInteger, BigInteger>[] topKIndexDistTupleB;
    private Pair<BigInteger, BigInteger>[] topKIndexDistTupleA;

    public TopKQuery(int bitSize){
        this.bitSize = bitSize;
        this.twoToL = BigInteger.TWO.pow(bitSize);
    }

    /**
     * Set (index, distance) tuple, which compute distance between each record sequence and query sequence
     * @param QA Query shared with party A
     * @param SA Data shared with party A
     * @param QB Query shared with party B
     * @param SB Data shared with party B
     * @param pool
     * @throws Exception
     */
    public void secureQueryPreCompute(BigInteger[] QA, BigInteger[][] SA, BigInteger[] QB, BigInteger[][] SB, OfflineShufflingPool pool) throws Exception{
        this.m = SA.length; //row length
        this.n = SA[0].length; //column length

        BigInteger[] dEDA;
        BigInteger[] dEDB;

        dEDA= new BigInteger[m];
        dEDB = new BigInteger[m];
        indexDistTupleB = new Pair[m];
        indexDistTupleA = new Pair[m];
        SecureExactEditDistance seed = new SecureExactEditDistance(bitSize);

        /*
         * compute distance between each record and query
         */
        for (int i = 0; i< m; i++){
            seed.setDistance(SA[i],SB[i],QA,QB, pool );
            dEDA[i]= seed.getDedA();
            dEDB[i] = seed.getDedB();
        }

        BigInteger[] indexA  = new BigInteger[m];
        BigInteger[] indexB  = new BigInteger[m];

        /*
         * Share index between two parties!
         */
        SecureRandom srand = new SecureRandom();
        for (int i= 0; i< m; i++){
            BigInteger rnd = new BigInteger(bitSize, srand);
            indexA[i] = rnd;
            indexB[i] = (BigInteger.valueOf(i).subtract(rnd)).mod(twoToL);
        }

        SSF ssf = new SSF(bitSize);

        int indexArraySize = indexA.length;
        Integer[] pi = ssf.getPi(indexArraySize);

        OfflineShuffling offlineShufflingIndex = new OfflineShuffling();
        OfflineShuffling offlineShufflingDist = new OfflineShuffling();

        BigInteger[] indexBPrime = ssf.getOfflineOutput(indexArraySize, offlineShufflingIndex, pi);
        BigInteger[] distBPrime = ssf.getOfflineOutput(indexArraySize,offlineShufflingDist,pi);

        BigInteger[] indexAPrime = ssf.getOnlineOutput(indexArraySize,indexB, indexA, offlineShufflingIndex, pi);
        BigInteger[] distAPrime = ssf.getOnlineOutput(indexArraySize, dEDB,dEDA,offlineShufflingDist,pi);


        for(int i = 0; i< m; i++){
            indexDistTupleB[i]= new ImmutablePair(indexBPrime[i], distBPrime[i]);
            indexDistTupleA[i]= new ImmutablePair(indexAPrime[i],distAPrime[i]);
        }
    }

    /**
     * Generate top k-NN (index, distance) pairs
     * @param k k-NN
     */
    public void genTopKIndexDistTuple(int k){
        if(k> m){
            System.out.println("k is larger than total records of database");
            throw new IndexOutOfBoundsException();
        }
        deltaA = new BigInteger[m][2]; //index 0 save index, and index 1 save distance;
        deltaB = new BigInteger[m][2];

        for(int i= 0; i< m; i++){
            deltaA[i][0]= indexDistTupleA[i].getLeft();
            deltaB[i][0]= indexDistTupleB[i].getLeft();
            deltaA[i][1]= indexDistTupleA[i].getRight();
            deltaB[i][1]= indexDistTupleB[i].getRight();
        }

        //check bubble sort !!!!
        for(int i= 1; i<= k;i++){
            for(int j= m-1; j>= i; j--){
                int theta = thetaHelper(deltaA[j][1], deltaB[j][1], deltaA[j-1][1], deltaB[j-1][1]);
                if(theta==0) {
                    /**
                     * Java pass by value, therefore, I can not create a method to do swap
                     */
                   BigInteger temp;
                   temp = deltaA[j-1][0];
                   deltaA[j-1][0] = deltaA[j][0];
                   deltaA[j][0]= temp;

                   temp = deltaB[j-1][0];
                   deltaB[j-1][0] = deltaB[j][0];
                   deltaB[j][0]= temp;

                    temp = deltaA[j-1][1];
                    deltaA[j-1][1] = deltaA[j][1];
                    deltaA[j][1]= temp;

                    temp = deltaB[j-1][1];
                    deltaB[j-1][1] = deltaB[j][1];
                    deltaB[j][1]= temp;
                }

            }
        }
        topKIndexDistTupleB = new Pair[k];
        topKIndexDistTupleA = new Pair[k];

        for (int i = 0; i < k; i++){
            topKIndexDistTupleB[i]= new ImmutablePair(deltaB[i][0], deltaB[i][1]);
            topKIndexDistTupleA[i]= new ImmutablePair(deltaA[i][0], deltaA[i][1]);
        }
    }


    /**
     * Getter method to get (index, distance) pair from unsorted results
     * @return get party B's (index, distance) pair
     */
    public Pair<BigInteger, BigInteger>[] getIndexDistTupleB(){
        return indexDistTupleB;
    }

    /**
     * Getter method to get (index, distance) pair from unsorted results for party A
     * @return get party A's (index, distance) pair
     */
    public Pair<BigInteger, BigInteger>[] getIndexDistTupleA(){
        return indexDistTupleA;
    }

    /**
     * Getter method to get top k-NN (index, distance) pair  for party B
     * @return get party B's top k-NN (index, distance) pair
     */
    public Pair<BigInteger, BigInteger>[] getTopKPairB(){
        return topKIndexDistTupleB;
    }

    /**
     * Getter method to get top k-NN (index, distance) pair  for party A
     * @return get party A's top k-NN (index, distance) pair
     */
    public Pair<BigInteger, BigInteger>[] getTopKPairA(){
        return topKIndexDistTupleA;
    }


    /**
     * Helper to avoid expensive GC computation
     * @param xA
     * @param xB
     * @param yA
     * @param yB
     * @return
     */
    private int thetaHelper(BigInteger xA, BigInteger xB, BigInteger yA, BigInteger yB){
        int result = (xA.add(xB).mod((BigInteger.TWO).pow(bitSize))).compareTo(yA.add(yB).mod((BigInteger.TWO).pow(bitSize)));
        if(result >0){
            return 1;
        }
        else return 0;
    }




}
