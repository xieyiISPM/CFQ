package secureCompare;

import secureShuffle.InitSet;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SecureCompare {
    private int bitSize = 10;
    private BigInteger twoToL = BigInteger.valueOf(2^(2*2024));
    private BigInteger r;
    public SecureCompare(){

        SecureRandom srand = new SecureRandom();
        this.r = new BigInteger(bitSize, srand);
    }
    public SecureCompare(int bitSize, BigInteger twoToL){
        this.bitSize = bitSize;
        this. twoToL = twoToL;
        SecureRandom srand = new SecureRandom();
        this.r = new BigInteger(bitSize, srand);
    }
    public int secureCompare(BigInteger distHa, BigInteger distCa, BigInteger distHb, BigInteger distCb){
        //C generate rA
        BigInteger rA = genR(distCa);
        //C generate rB
        BigInteger rB = genR(distCb);
        //Ha generate distPrime
        BigInteger distHaPrime = genDistPrime(distHa, rA);
        BigInteger distHbPrime = genDistPrime(distHb, rB);
        return distHaPrime.compareTo(distHbPrime);
    }

    private BigInteger genR(BigInteger dist){

        BigInteger rDist = (dist.add(r)).mod(twoToL);
        return rDist;
    }

    private BigInteger genDistPrime(BigInteger dist, BigInteger rDist){
        return dist.add(rDist).mod(twoToL);
    }


}
