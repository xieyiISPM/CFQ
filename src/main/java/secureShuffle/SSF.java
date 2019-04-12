package secureShuffle;

import com.n1analytics.paillier.PaillierPrivateKey;
import com.n1analytics.paillier.PaillierPublicKey;

import java.math.BigInteger;

public class SSF {
    int bitSize = 10;
    BigInteger twoToL = (BigInteger.TWO).pow(bitSize);

    public SSF(){

    }

    public SSF(int bitSize){
        this.bitSize = bitSize;
        this.twoToL = (BigInteger.TWO).pow(bitSize);
    }
    public BigInteger[] getOfflineOutput(int arraySize, OfflineShuffling offlineShuffling, int[] pi){
        BigInteger twoToL = (BigInteger.TWO).pow(bitSize);
        PaillierPrivateKey paillierPrivateKey = PaillierPrivateKey.create(1024);
        PaillierPublicKey paillierPublicKey = paillierPrivateKey.getPublicKey();
        BigInteger[] L0 = offlineShuffling.genL0(arraySize, bitSize, paillierPublicKey);
        BigInteger[] L1 = offlineShuffling.genL1(arraySize, bitSize, twoToL, L0, pi, paillierPublicKey);
        BigInteger[] L2 = offlineShuffling.genL2(L1, twoToL, paillierPrivateKey);
        return L2;
    }

    public int[] getPi(int arraySize){
        InitSet initSet = new InitSet();
        int[] pi = initSet.genPi(arraySize);
        return pi;
    }

    public BigInteger[] getOnlineOuptut(int arraySize, BigInteger[] xH, BigInteger[] xC, OfflineShuffling offlineShuffling, int[] pi ) throws Exception {
        OnlineShuffling onlineShuffling = new OnlineShuffling();

        BigInteger[] L3 = onlineShuffling.genL3(offlineShuffling.V, xH, twoToL);
        BigInteger[] L4 = onlineShuffling.genL4(offlineShuffling.U, L3, xC, twoToL, pi);
        return L4;
    }

}
