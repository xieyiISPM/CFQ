package afgh;

import java.math.BigInteger;
import java.net.SecureCacheResponse;
import java.security.SecureRandom;

public class AFGHKeyGen {
    private int keySize = 1024;
    private int bitToByte = 8;
    public AFGHKeyGen(){

    }
    public AFGHKeyGen ( int keySize){
        this.keySize = keySize;
    }

    public BigInteger genPrivateKey(){
        BigInteger sk;
        SecureRandom secureRandom = new SecureRandom();
        byte bytes[] = new byte[keySize/bitToByte];
        secureRandom.nextBytes(bytes);
        sk = new BigInteger(1,bytes);
        return sk;
     }
}
