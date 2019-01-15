import afgh.AFGHKeyGen;

import java.math.BigInteger;

public class ProxyTest {

    public static void main(String[]  args){
        AFGHKeyGen afgh = new AFGHKeyGen(1024);
        BigInteger sk = afgh.genPrivateKey();
        System.out.println("private key: " + sk);
        System.out.println("private key bitlength: " + sk.bitLength());
    }
}
