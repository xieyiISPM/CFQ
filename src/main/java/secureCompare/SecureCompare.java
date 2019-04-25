package secureCompare;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class SecureCompare {
    private int bitSize = 100;
    private BigInteger twoToL = BigInteger.TWO.pow(bitSize);

    public SecureCompare(){
    }

    public SecureCompare(int bitSize){
        this.bitSize = bitSize;
        this.twoToL = BigInteger.TWO.pow(bitSize);
    }

/*    public SecureCompare(int bitSize, BigInteger twoToL){
        this.bitSize = bitSize;
        this.twoToL = twoToL;
    }*/
    public int secureCompare(BigInteger distHa, BigInteger distCa, BigInteger distHb, BigInteger distCb){
        Random srand = new SecureRandom();
        int testTimes = 3;
        BigInteger[] r = new BigInteger[testTimes];
        int[] result = new int[testTimes];
        for(int i = 0; i<testTimes; i++){
            r[i] = new BigInteger((bitSize *(i+1))/testTimes, srand);
            //C generate rA
            BigInteger blindedCa = blindedDist(distCa,r[i] );
            //C generate rB
            BigInteger blindedCb = blindedDist(distCb,r[i] );
            //Ha generate distPrime
            BigInteger distHaPrime = genDistPrime(distHa, blindedCa);
            BigInteger distHbPrime = genDistPrime(distHb, blindedCb);
            result[i]= distHaPrime.compareTo(distHbPrime);

        }
        int voteResult= 0;
        for(int i = 0; i < testTimes; i++){
            voteResult = result[i] + voteResult;
        }

        if (voteResult > 0 ){
            return 1;
        }
        else if(voteResult ==0){
            return 0;
        }
        else return -1;

        /*System.out.print("random r:");
        System.out.println(r);*/
        /* System.out.print("distHaPrime:");
        System.out.println(distHaPrime);
        System.out.print("distHbPrime: ");
        System.out.println(distHbPrime);*/
    }

    private BigInteger blindedDist(BigInteger dist, BigInteger r){

        BigInteger distPlusR = (dist.add(r)).mod(twoToL);
        return distPlusR;
    }

    private BigInteger genDistPrime(BigInteger dist, BigInteger blindedDist){

        return dist.add(blindedDist).mod(twoToL);
    }


}
