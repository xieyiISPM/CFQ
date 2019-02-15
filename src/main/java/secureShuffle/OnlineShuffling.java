package secureShuffle;

import java.math.BigInteger;

public class OnlineShuffling {
    public OnlineShuffling(){

    }

    public BigInteger[] genL3(BigInteger[] vArray, BigInteger[] xH, BigInteger twoToL) throws  Exception{
        if(vArray.length != xH.length){
            throw new ArrayIndexOutOfBoundsException();
        }
        BigInteger[] L3 =  new BigInteger[vArray.length];
        for(int i= 0; i< L3.length; i++){
            L3[i] = (xH[i].add(vArray[i])).mod(twoToL);
        }
        return L3;
    }

    public BigInteger[] genL4(BigInteger[] uArray, BigInteger[] L3, BigInteger[] xC, BigInteger twoToL, int[] pi) throws Exception{
        if((uArray.length != xC.length) || (L3.length != xC.length)){
            throw new ArrayIndexOutOfBoundsException();
        }
        BigInteger[] L4 = new BigInteger[uArray.length];
        for(int i= 0; i < L4.length; i++){
            L4[i] = ((L3[i].add(xC[i])).add(uArray[i])).mod(twoToL);
        }
        InitSet initSet = new InitSet();
        return initSet.permRandomArray(L4,pi);
    }
}
