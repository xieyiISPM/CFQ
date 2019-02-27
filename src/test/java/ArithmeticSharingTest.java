import java.math.BigInteger;
import java.security.SecureRandom;

public class ArithmeticSharingTest {
    public static void main(String[] args){
        SecureRandom srand = new SecureRandom();
        int bitSize = 10;
        boolean testResult = true;
        long modular = (long)Math.pow(2,bitSize);
        BigInteger m = BigInteger.valueOf(modular);
        for(int i =0; i< 1000; i++) {
            BigInteger[] x = genShares(bitSize, m);
            BigInteger[] y = genShares(bitSize, m);
            //System.out.println("Restore value for x:" + testRestore(x[0], x[1], m, x[2]));
            //System.out.println("restore value for y:" + testRestore(y[0],y[1], m, y[2]));
            if(!testSum(x, y, m)){
                System.out.println("Sum test fails!");
                testResult = false;
                break;
            }
        }
        if(testResult){
            System.out.println("Sum test passes!");
        }
    }

    private static boolean testRestore(BigInteger x0, BigInteger x1, BigInteger m, BigInteger x){
        BigInteger sum = (x0.add(x1)).mod(m);
        System.out.println("m = " + m);
        System.out.println("share0 = " + x0);
        System.out.println("share1 = " + x1);
        System.out.println("Origin = " + x);
        System.out.println("sum = " + sum);
        return sum.equals(x);
    }

    private static BigInteger restore(BigInteger[] x, BigInteger m){
        BigInteger sum = (x[0].add(x[1])).mod(m);
        return sum;
    }

    private static BigInteger[] genShares(int bitSize, BigInteger m){
        SecureRandom srand = new SecureRandom();
        BigInteger[] x  = new BigInteger[3];
        x[2] = new BigInteger(bitSize, srand);
        x[0] = new BigInteger(bitSize, srand);
        x[1] = (x[2].subtract(x[0])).mod(m);
        return x;
    }

    private static boolean testSum(BigInteger[] x, BigInteger[] y, BigInteger m){
        BigInteger[] z = new BigInteger[3];
        z[0] = x[0].add(y[0]).mod(m);
        z[1] = x[1].add(y[1]).mod(m);
        z[2] = restore(z, m);
        //System.out.println("Restore value for z: ");
        //testRestore(z[0], z[1], m, z[2]);
        return z[2].equals((x[2].add(y[2])).mod(m));
    }
}
