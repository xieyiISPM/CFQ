import com.google.common.base.Stopwatch;
import com.n1analytics.paillier.PaillierPrivateKey;
import com.n1analytics.paillier.PaillierPublicKey;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PaillierTest {
    public static void main(String[] args){

        Stopwatch stopwatch = Stopwatch.createStarted();
        TimeUnit timeUnit = TimeUnit.MICROSECONDS;
        PaillierPrivateKey sk = PaillierPrivateKey.create(1024);
        PaillierPublicKey pk = sk.getPublicKey();

        System.out.println("public key generator:" + pk.getGenerator());
        System.out.println("Modular : " + pk.getModulus());
        BigInteger plainText = new BigInteger("12354");
        BigInteger cypher = pk.raw_encrypt(plainText);
        System.out.println("cypher: " + cypher);
        BigInteger pt = sk.raw_decrypt(cypher);
        System.out.println("plainText: " + pt);
        System.out.println("timer(ms): " + stopwatch.elapsed(timeUnit) + " ms");


    }
}
