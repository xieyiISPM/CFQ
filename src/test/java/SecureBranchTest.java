import secureBranch.SecureBranch;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SecureBranchTest {
    public static void main(String[] args) throws Exception{
        int bitSize = 5;
        SecureRandom srand = new SecureRandom();
        BigInteger m = (BigInteger.TWO).pow(bitSize);

        //Generate two shares from a number. arr[2] store original number
        BigInteger[] xA  = genShares(bitSize, m);
        printArr(xA,"xA");

        BigInteger[] xB  = genShares(bitSize, m);
        printArr(xB,"xB");

        System.out.println("X0 reconstruct: " + reconstruct(xA[0], xB[0], bitSize));
        System.out.println("X1 reconstruct: " + reconstruct(xA[1], xB[1], bitSize));
        System.out.println();

        BigInteger[] yA  = genShares(bitSize, m);
        printArr(yA,"yA");

        BigInteger[] yB  = genShares(bitSize, m);
        printArr(yB,"yB");

        System.out.println("y0 reconstruct: " + reconstruct(yA[0], yB[0], bitSize));
        System.out.println("y1 reconstruct: " + reconstruct(yA[1], yB[1], bitSize));
        System.out.println();

        SecureBranch sb = new SecureBranch(bitSize);


        sb.addAndCompare(xA,xB, yA, yB);

        System.out.println("YA: " + sb.getYA());
        System.out.println("YB: "+ sb.getYB());

    }
    private static BigInteger[] genShares(int bitSize, BigInteger m) {
        SecureRandom srand = new SecureRandom();
        BigInteger[] arr = new BigInteger[2];
        BigInteger sum = new BigInteger(bitSize, srand);
        arr[0] = new BigInteger(bitSize, srand);
        arr[1] = (sum.subtract(arr[0])).mod(m);
        return arr;
    }

    private static void printArr(BigInteger[] arr, String varName){

        int i = 0;
        for(BigInteger bi: arr){
            System.out.println(varName + " " + i + ": " + bi);
            i++;
        }
        System.out.println();
    }

    private static BigInteger reconstruct(BigInteger a, BigInteger b, int bitSize){
        BigInteger m = (BigInteger.TWO).pow(bitSize);
        BigInteger sum = (a.add(b)).mod(m);
        return sum;
    }
}
