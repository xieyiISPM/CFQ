import secureCompare.SecureCompare;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SecureCompareTest {
    public static void main(String[] args){
        System.out.println(secureCompareTest());
    }

    private static String secureCompareTest(){
        SecureCompare sc = new SecureCompare();
        SecureRandom srand = new SecureRandom();
        int bitSize = 10;
        BigInteger twoToL =  BigInteger.valueOf(2^(2*2024));

        for(int testRound = 0; testRound < 100000; testRound++) {
            BigInteger distHa = new BigInteger(bitSize, srand);
           // System.out.print("distHa: ");
            //System.out.println(distHa);
            BigInteger distHb = new BigInteger(bitSize, srand);
            //System.out.print("distHb: ");
            //System.out.println(distHb);
            BigInteger distCa = new BigInteger(bitSize, srand);
            BigInteger distCb = new BigInteger(bitSize, srand);
            int rawCompareResult = ((distHa.add(distCa)).mod(twoToL)).compareTo((distHb.add(distCb)).mod(twoToL));
            int secureCompareResult = sc.secureCompare(distHa, distCa, distHb, distCb);
            if (rawCompareResult != secureCompareResult) {
                System.out.print("distHa: ");
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
                System.out.println("test round= " + testRound);
                return "Secure compare test failed";
            }
        }

        return "Secure compare test passed";

    }
}
