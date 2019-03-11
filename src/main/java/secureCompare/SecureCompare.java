package secureCompare;

import secureShuffle.InitSet;

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

    public SecureCompare(int bitSize, BigInteger twoToL){
        this.bitSize = bitSize;
        this.twoToL = twoToL;
    }
    public int secureCompare(BigInteger distHa, BigInteger distCa, BigInteger distHb, BigInteger distCb){
        Random srand = new SecureRandom();
        BigInteger r = new BigInteger(bitSize,srand);
        //C generate rA
        BigInteger rA = genR(distCa,r );
        //C generate rB
        BigInteger rB = genR(distCb,r );
        //Ha generate distPrime
        BigInteger distHaPrime = genDistPrime(distHa, rA);
        BigInteger distHbPrime = genDistPrime(distHb, rB);

        /*System.out.print("distHaPrime");
        System.out.println(distHaPrime);
        System.out.print("distHbPrime");
        System.out.println(distHbPrime);*/

        return distHaPrime.compareTo(distHbPrime);
    }

    private BigInteger genR(BigInteger dist, BigInteger r){

        BigInteger rDist = (dist.add(r)).mod(twoToL);
        return rDist;
    }

    private BigInteger genDistPrime(BigInteger dist, BigInteger rDist){

        return dist.add(rDist).mod(twoToL);
    }


}
