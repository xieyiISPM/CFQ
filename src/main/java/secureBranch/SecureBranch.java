package secureBranch;

import gc.GarbledCircuit;
import secureShuffle.OfflineShuffling;
import secureShuffle.SSF;

import java.math.BigInteger;

public class SecureBranch {
    private int bitSize = 10;
    private BigInteger yOutputA = null;
    private BigInteger yOutputB = null;


    public SecureBranch(int bitSize){
        this.bitSize = bitSize;
    }

    public void addAndComare(BigInteger[] xA, BigInteger[] xB, BigInteger[] yA, BigInteger[] yB, String serverOutputFile, String clientOutputFile) throws Exception{
        if(xA.length != xB.length || yA.length!= yB.length || xA.length != yA.length || xA.length != 2){
            throw new IllegalArgumentException("Array sizes do not match!");
        }

        int arraySizeX = xA.length;
        int arraySizeY = yA.length;


        SSF ssf = new SSF(bitSize);
        int[] pi = ssf.getPi(arraySizeX);
        OfflineShuffling offlineShufflingX = new OfflineShuffling();
        BigInteger[] xAPrime = ssf.getOfflineOutput(arraySizeX, offlineShufflingX);
        BigInteger[] xBPrime = ssf.getOnlineOuptut(arraySizeX,xA, xB,offlineShufflingX,pi );

        OfflineShuffling offlineShufflingY = new OfflineShuffling();
        BigInteger[] yHPrime = ssf.getOfflineOutput(arraySizeX, offlineShufflingY);
        BigInteger[] yCPrime = ssf.getOnlineOuptut(arraySizeX,xA, xB,offlineShufflingY,pi );

        GarbledCircuit gc = new GarbledCircuit();
        //input file will be xAPrime, and xBPrime;
        int theta = gc.add_cmp(serverOutputFile, clientOutputFile);

        //check which should be assigned to yOutputA/yOutputB
        if(theta == 1){
            yOutputA = yHPrime[0];
            yOutputB = yCPrime[0];

        }
        else{
            yOutputA = yHPrime[1];
            yOutputB = yCPrime[1];
        }

    }

    public BigInteger[] genArray(BigInteger bigInt1, BigInteger bigInt2){
        BigInteger[] arr= new BigInteger[2];
        arr[0] = bigInt1;
        arr[1] = bigInt2;
        return arr;

    }

    public BigInteger getYA(){
        return yOutputA;
    }

    public BigInteger getYB(){
        return yOutputB;
    }




}
