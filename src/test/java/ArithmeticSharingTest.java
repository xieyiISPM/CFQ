import com.n1analytics.paillier.PaillierPrivateKey;
import com.n1analytics.paillier.PaillierPublicKey;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class ArithmeticSharingTest {
    public static void main(String[] args){
        SecureRandom srand = new SecureRandom();
        int bitSize = 10; //NOTE: bitSize < Pailliar modular size /2 !!!!
        boolean testResult = true;
        BigInteger m = (BigInteger.TWO).pow(bitSize);
        int testRound = 10;

        //Test generate arithmetic share
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

        //test generate arithmetic MT via HE (Pailliar) based on paper ABY
        /*for(int i =0; i< testRound; i++) {
            List<BigInteger[]> mtList = genPailliarArithmeticMT(bitSize, m);

            if(!mtList.get(2)[2].equals((mtList.get(0)[2].multiply(mtList.get(1)[2]).mod(m)))){
                testResult = false;
                System.out.println("c = " + mtList.get(2)[2]);
                break;
            }
        }

        if(testResult){
            System.out.println("Pailliar MT test passed!");
        }
        else{
            System.out.println("Pailliar MT test fails!");
        }*/


        //Test multiplication
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
       /* BigInteger[] a = genShares(bitSize, m);
        BigInteger[] b = genShares(bitSize,m);*/

        BigInteger[] a = new BigInteger[3];
        BigInteger[] b = new BigInteger[3];

        List<BigInteger[]> mtList = genPailliarArithmeticMT(bitSize, m);
        for(int i = 0; i< 3; i++){
            a[i] = mtList.get(0)[i];
            b[i] = mtList.get(1)[i];
            c[i] = mtList.get(2)[i];

        }

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


    private static List<BigInteger[]> genPailliarArithmeticMT(int bitSize, BigInteger m){
        BigInteger[] a = genShares(bitSize, m);
        BigInteger[] b = genShares(bitSize,m);
        SecureRandom srand = new SecureRandom();
        BigInteger r = new BigInteger(bitSize, srand);
        BigInteger[] c = new BigInteger[3];
        c[1] = ((a[1].multiply(b[1]).mod(m)).subtract(r)).mod(m);

        int modulusLength = 256;
        PaillierPrivateKey sk = PaillierPrivateKey.create(modulusLength);
        PaillierPublicKey pk = sk.getPublicKey();
        BigInteger cypherA0 = pk.raw_encrypt(a[0]);
        BigInteger cypherB0 = pk.raw_encrypt(b[0]);
        BigInteger cypherR = pk.raw_encrypt(r);

        BigInteger cypherA0ToB1 = pk.raw_multiply(cypherA0, b[1]);
        BigInteger cypherB0ToA1 = pk.raw_multiply(cypherB0, a[1]);
        BigInteger temp = pk.raw_add(cypherA0ToB1, cypherB0ToA1);
        BigInteger d = pk.raw_add(temp, cypherR);

        c[0] = ((a[0].multiply(b[0]).mod(m)).add(sk.raw_decrypt(d))).mod(m);
        c[2] = restore(c,m);

        List<BigInteger[]> mt = new ArrayList<>();
        mt.add(a);
        mt.add(b);
        mt.add(c);
        return mt;
    }

}
