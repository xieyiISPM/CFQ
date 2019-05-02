import secureCompare.SecureCompare;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SecureCompareTest {
    public static void main(String[] args){

        System.out.println(secureCompareTest());
    }

    private static double secureCompareTest(){
        SecureRandom srand = new SecureRandom();
        int bitSize = 12;
        int distBitSize = 5;
       // int startValue = Integer.MAX_VALUE;
        int startValue = 0;

        SecureCompare sc = new SecureCompare(bitSize);

        BigInteger twoToL =  BigInteger.TWO.pow(bitSize);
        long totalTestRound = 1000000;
        int failTimes= 0;
        for( int testRound = 0; testRound < totalTestRound; testRound++) {
            BigInteger randA = new BigInteger(distBitSize,srand);
            BigInteger distA = randA.add(BigInteger.valueOf(startValue)); //generate distA between 1024 to 2048
            //BigInteger distA = BigInteger.ONE;
            BigInteger distCa = new BigInteger(bitSize, srand);
            BigInteger distHa =((distA.subtract(distCa)).mod(twoToL));

            BigInteger randB = new BigInteger(distBitSize, srand);
            BigInteger distB = randB.add(BigInteger.valueOf(startValue));
            //BigInteger distB = BigInteger.valueOf(255);
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
                System.out.println();*/
                System.out.println("rawCompareResult: " + rawCompareResult);
                System.out.println("secureCompareResult: " + secureCompareResult );
                System.out.println("test round= " + testRound);
                failTimes++;
            }
            //System.out.println();
        }
        System.out.println(failTimes);
        return (double)failTimes/totalTestRound;

    }
}
