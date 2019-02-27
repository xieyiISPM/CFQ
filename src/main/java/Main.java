import java.math.BigInteger;
import java.security.SecureRandom;

public class Main {
    public static void main(String[] args){
        SecureRandom srand = new SecureRandom();
        int bitSize = 10;
        BigInteger m =  BigInteger.valueOf(2^(2*10));
        BigInteger x = new BigInteger(bitSize, srand);
        BigInteger x0 = new BigInteger(bitSize, srand);
        BigInteger x1 = x.subtract(x0).mod(m);
        System.out.println(testSum(x0, x1, m, x));


    }

    private static boolean testSum(BigInteger x0, BigInteger x1, BigInteger m, BigInteger x){
        BigInteger sum = x0.add(x1).mod(m);
        System.out.println("x0 = " + x0);
        System.out.println("x1 = " + x1);
        System.out.println("sum value: "+ sum);
        System.out.println("Orginal x: " + x);
        return sum.equals(x);
    }
}
