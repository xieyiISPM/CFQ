package secureMinimumSelection;

import gc.GarbledCircuit;
import secureShuffle.OfflineShuffling;
import secureShuffle.SSF;
import java.math.BigInteger;
import java.rmi.server.ExportException;

public class SecureMiniSelection {
    private BigInteger xMinA;
    private BigInteger xMinB;
    private BigInteger[] xAPrime;
    private BigInteger[] xBPrime;
    private int bitSize = 10;
    public SecureMiniSelection(int bitSize){
        this.bitSize = bitSize;
    }

    public void getMini(BigInteger[] xA, BigInteger[] xB, String serverOutputFile, String clientOutputFile) throws Exception {
        if(xA.length != xB.length){
            throw new IllegalArgumentException("Array sizes do not match!");
        }

        SSF ssf = new SSF(bitSize);
        int arraySize = xA.length;
        int[] pi = ssf.getPi(arraySize);

        OfflineShuffling offlineShufflingX = new OfflineShuffling();
        xAPrime = ssf.getOfflineOutput(arraySize, offlineShufflingX);
        xBPrime = ssf.getOnlineOuptut(arraySize,xA, xB,offlineShufflingX,pi );

        BigInteger xDeltaA = xAPrime[0];
        BigInteger xDeltaB = xBPrime[0];

        for (int i = 0; i< xA.length-1;i++){
            GarbledCircuit gc = new GarbledCircuit();
            //input file will be xDeltaA, and xPrime[i+1];
            int theta = gc.add_cmp(serverOutputFile, clientOutputFile);
            if(theta==1){
                xDeltaA = xAPrime[i+1];
                xDeltaB = xBPrime[i+1];
            }

        }

        xMinA = xDeltaA;
        xMinB = xDeltaB;

    }

    public BigInteger getxMinA(){
        return xMinA;
    }

    public BigInteger getMinB(){
        return xMinB;
    }

}
