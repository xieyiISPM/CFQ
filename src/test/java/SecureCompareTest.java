import secureCompare.SecureCompare;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SecureCompareTest {
    public static void main(String[] args){

        System.out.println(secureCompareTest());
    }

    private static double secureCompareTest(){
        SecureRandom srand = new SecureRandom();
        int bitSize = 10;
        SecureCompare sc = new SecureCompare(bitSize);

        BigInteger twoToL =  BigInteger.TWO.pow(bitSize);
        long totalTestRound = 100000;
        int failTimes= 0;
        for( int testRound = 0; testRound < totalTestRound; testRound++) {
            //BigInteger distA = new BigInteger(bitSize-1,srand);
            BigInteger distA = BigInteger.ONE;
            BigInteger distCa = new BigInteger(bitSize, srand);
            BigInteger distHa =((distA.subtract(distCa)).mod(twoToL));

            //BigInteger distB = new BigInteger(bitSize-1,srand);
            BigInteger distB = BigInteger.valueOf(255);
            BigInteger distCb = new BigInteger(bitSize, srand);
            BigInteger distHb =((distB.subtract(distCb)).mod(twoToL));

            /*System.out.print("distA: ");
            System.out.println(distA);
            System.out.print("distB: ");
            System.out.println(distB);
            System.out.println();*/
            int rawCompareResult = (distA.compareTo(distB));
            int secureCompareResult = sc.secureCompare(distHa, distCa, distHb, distCb);
            /*System.out.print("distHa: ");
            System.out.println(distHa);
            System.out.print("distCa: ");
            System.out.println(distCa);
            System.out.print("distHb: ");
            System.out.println(distHb);
            System.out.print("distCb: ");
            System.out.println(distCb);
            System.out.println();*/
            if (rawCompareResult != secureCompareResult) {
                /*System.out.print("distHa: ");
                System.out.println(distHa);
                System.out.print("distCa: ");
                System.out.println(distCa);
                System.out.print("distHb: ");
                System.out.println(distHb);
                System.out.print("distCb: ");
                System.out.println(distCb);
                System.out.println();
                System.out.println("rawCompareResult: " + rawCompareResult);
                System.out.println("secureCompareResult: " + secureCompareResult );
                System.out.println("test round= " + testRound);*/
                failTimes++;
            }
            //System.out.println();
        }
        System.out.println(failTimes);
        return (double)failTimes/totalTestRound;

    }
}
