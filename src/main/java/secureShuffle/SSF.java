package secureShuffle;

import com.n1analytics.paillier.PaillierPrivateKey;
import com.n1analytics.paillier.PaillierPublicKey;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SSF {
    int bitSize = 10;
    BigInteger twoToL = (BigInteger.TWO).pow(bitSize);


    public SSF(){

    }

    public SSF(int bitSize){
        this.bitSize = bitSize;
        this.twoToL = (BigInteger.TWO).pow(bitSize);
    }
    public BigInteger[] getOfflineOutput(int arraySize, OfflineShuffling offlineShuffling, Integer[] pi){
        BigInteger twoToL = (BigInteger.TWO).pow(bitSize);
        PaillierPrivateKey paillierPrivateKey = PaillierPrivateKey.create(1024);
        PaillierPublicKey paillierPublicKey = paillierPrivateKey.getPublicKey();
        BigInteger[] L0 = offlineShuffling.genL0(arraySize, bitSize, paillierPublicKey);
        BigInteger[] L1 = offlineShuffling.genL1(arraySize, bitSize, twoToL, L0, pi, paillierPublicKey);
        BigInteger[] L2 = offlineShuffling.genL2(L1, twoToL, paillierPrivateKey);
        return L2;
    }

    public Integer[] getPi(int arraySize){
        InitSet initSet = new InitSet();
        Integer[] pi = initSet.genPi(arraySize);
        return pi;
    }

    public BigInteger[] getOnlineOutput(int arraySize, BigInteger[] xB, BigInteger[] xA, OfflineShuffling offlineShuffling, Integer[] pi ) throws Exception {
        OnlineShuffling onlineShuffling = new OnlineShuffling();

        BigInteger[] L3 = onlineShuffling.genL3(offlineShuffling.V, xB, twoToL);
        BigInteger[] L4 = onlineShuffling.genL4(offlineShuffling.U, L3, xA, twoToL, pi);
        return L4;
    }



}
