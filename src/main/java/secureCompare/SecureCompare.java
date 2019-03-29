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
        BigInteger r = new BigInteger(bitSize,srand);

        //C generate rA
        BigInteger blindedCa = blindedDist(distCa,r );
        //C generate rB
        BigInteger blindedCb = blindedDist(distCb,r );
        //Ha generate distPrime
        BigInteger distHaPrime = genDistPrime(distHa, blindedCa);
        BigInteger distHbPrime = genDistPrime(distHb, blindedCb);


        /*System.out.print("random r:");
        System.out.println(r);*/
        /* System.out.print("distHaPrime:");
        System.out.println(distHaPrime);
        System.out.print("distHbPrime: ");
        System.out.println(distHbPrime);*/


        return distHaPrime.compareTo(distHbPrime);
    }

    private BigInteger blindedDist(BigInteger dist, BigInteger r){

        BigInteger distPlusR = (dist.add(r)).mod(twoToL);
        return distPlusR;
    }

    private BigInteger genDistPrime(BigInteger dist, BigInteger blindedDist){

        return dist.add(blindedDist).mod(twoToL);
    }


}
