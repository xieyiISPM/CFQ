import com.n1analytics.paillier.PaillierPrivateKey;
import com.n1analytics.paillier.PaillierPublicKey;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ArithmeticSharingTest {
    public static void main(String[] args){
        SecureRandom srand = new SecureRandom();
        int bitSize = 5;
        boolean testResult = true;
        long modular = (long)Math.pow(2,bitSize);
        BigInteger m = BigInteger.valueOf(modular);
        int testRound = 10000;
        /*for(int i =0; i< 1000; i++) {
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
        }*/
        /*BigInteger [] c = genArithmeticMT(bitSize, m);
        System.out.println(c[0]);
        System.out.println(c[1]);
        System.out.println("c = " + restore(c,m));*/
        for(int i= 0; i < testRound; i++){
            BigInteger[] x = genShares(bitSize, m);
            BigInteger[] y = genShares(bitSize, m);
            if(!testMultiplication(x, y,bitSize, m)){
                testResult = false;
                break;
            }
        }
        if(testResult){
            System.out.println("Multiplication test passed!");
        }
        else{
            System.out.println("Multiplication test fails!");
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

    /**
     * Generate two shares from a number.
     * Note: arr[2] store original number
     * @param bitSize security parameter
     * @param m modular number
     * @return shares and original number
     */
    private static BigInteger[] genShares(int bitSize, BigInteger m){
        SecureRandom srand = new SecureRandom();
        BigInteger[] arr  = new BigInteger[3];
        arr[2] = new BigInteger(bitSize, srand);
        arr[0] = new BigInteger(bitSize, srand);
        arr[1] = (arr[2].subtract(arr[0])).mod(m);
        return arr;
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

    private static boolean testMultiplication(BigInteger[] x, BigInteger[] y, int bitSize, BigInteger m){
        BigInteger[] c = new BigInteger[3];
        BigInteger[] a = genShares(bitSize, m);
        BigInteger[] b = genShares(bitSize,m);

        SecureRandom srand = new SecureRandom();
        c[0] = new BigInteger(bitSize, srand);
        c[2] = a[2].multiply(b[2]).mod(m);
        c[1] = (c[2].subtract(c[0])).mod(m);
        BigInteger[] e = new BigInteger[3];
        BigInteger[] f = new BigInteger[3];
        e[0]= x[0].subtract(a[0]).mod(m);
        e[1] = x[1].subtract(a[1]).mod(m);
        e[2] =e[0].add(e[1]).mod(m);

        f[0]= y[0].subtract(b[0]).mod(m);
        f[1] = y[1].subtract(b[1]).mod(m);
        f[2] =f[0].add(f[1]).mod(m);

        BigInteger[] z = new BigInteger[3];
        z[0] = f[2].multiply(a[0]).add(e[2].multiply(b[0])).add(c[0]).mod(m);
        z[1] = e[2].multiply(f[2]).add(f[2].multiply(a[1])).add(e[2].multiply(b[1])).add(c[1]).mod(m);
        z[2] = restore(z, m);

        BigInteger mul = (x[2].multiply(y[2])).mod(m);

        return mul.equals(z[2]);
    }

    //error - need figure out
    private static BigInteger[] genArithmeticMT(int bitSize, BigInteger m){
        BigInteger[] c = new BigInteger[3];
        BigInteger[] a = genShares(bitSize, m);
        BigInteger[] b = genShares(bitSize,m);
        SecureRandom srand = new SecureRandom();
        BigInteger r = new BigInteger(bitSize, srand);
        c[1] = ((a[1].multiply(b[1]).mod(m)).subtract(r)).mod(m);

        System.out.println(" a = " + restore(a,m));
        System.out.println("b = " +restore(b,m));

        PaillierPrivateKey sk = PaillierPrivateKey.create(1024);
        PaillierPublicKey pk = sk.getPublicKey();
        BigInteger cypherA0 = pk.raw_encrypt(a[0]);
        BigInteger cypherB0 = pk.raw_encrypt(b[0]);
        BigInteger cypherR = pk.raw_encrypt(r);
        BigInteger d = (cypherA0.modPow(b[1], m).multiply(cypherB0.modPow(a[1],m)).multiply(cypherR)).mod(pk.getModulusSquared());
        c[0] = (a[0].multiply(b[0])).add(sk.raw_decrypt(d)).mod(m);
        return c;
    }

}
