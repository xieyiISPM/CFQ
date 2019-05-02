package secureMinimumSelection;

import secureShuffle.OfflineShuffling;
import secureShuffle.SSF;
import java.math.BigInteger;

/**
 * Secure minimum selection protocol
 */
public class SecureMiniSelection {
    private BigInteger xMinA;
    private BigInteger xMinB;
    private BigInteger[] xAPrime;
    private BigInteger[] xBPrime;
    private int bitSize = 10;
    public SecureMiniSelection(int bitSize){
        this.bitSize = bitSize;
    }

    /**
     * Generate minimum edit distance and save it to private field xMinA and xMinB
     * @param xA A inputs array
     * @param xB B inputs array
     * @throws Exception
     */
    public void getMini(BigInteger[] xA, BigInteger[] xB) throws Exception {
        if(xA.length != xB.length){
            throw new IllegalArgumentException("Array sizes do not match!");
        }

        SSF ssf = new SSF(bitSize);
        int arraySize = xA.length;
        Integer[] pi = ssf.getPi(arraySize);

        OfflineShuffling offlineShufflingX = new OfflineShuffling();
        xAPrime = ssf.getOfflineOutput(arraySize, offlineShufflingX,pi);
        xBPrime = ssf.getOnlineOutput(arraySize,xB, xA,offlineShufflingX,pi );

        /*System.out.print("xAPrime:");
        printArr(xAPrime);
        System.out.print("xBPrime: ");
        printArr(xBPrime);*/

        BigInteger xDeltaA = xAPrime[0];
        BigInteger xDeltaB = xBPrime[0];
       // GarbledCircuit addcmpGC = new GarbledCircuit("ADD-CMP.cir", "b-input", "a-input", "GCParser/results/siclientout", "GCParser/results/siserverout");

        for (int i = 1; i< xA.length;i++){
         //   int theta = addcmpGC.GCADDCMPOutPut(xDeltaA, xDeltaB, xAPrime[i], xBPrime[i]);
            int theta = thetaHelper(xDeltaA,xDeltaB,xAPrime[i],xBPrime[i]);
            //System.out.println("theta = "+ theta );

            if(theta == 1){
                xDeltaA = xAPrime[i];
                xDeltaB = xBPrime[i];
                /*System.out.println("xDeltaA: " + xDeltaA);
                System.out.println("xDeltaB: " + xDeltaB);*/
            }
        }
        xMinA = xDeltaA;
        xMinB = xDeltaB;
    }

    /**
     * Helper method combine individual value to an array
     * @param args BigInteger values
     * @return return a bigInteger array
     */
    public BigInteger[] genArray(BigInteger... args){
        BigInteger[] arr = new BigInteger[args.length];
        for(int i = 0; i< args.length; i++ ){
            arr[i] =  args[i];
        }
        return arr;
    }


    /**
     * Getter method for xMinA
     * @return
     */
    public BigInteger getMinA(){
        return xMinA;
    }

    /**
     * Getter method for xMinB
     * @return
     */
    public BigInteger getMinB(){
        return xMinB;
    }

    /**
     * Array screen printer helper method
     * @param arr
     */
    private void printArr(BigInteger[] arr){
        for (BigInteger item: arr){
            System.out.print(item + " ");
        }
        System.out.println();
    }

    /**
     * A helper function to avoid expensive GC computation
     * @param xA A's x share
     * @param xB B's x share
     * @param yA A's y share
     * @param yB B's y share
     * @return if x >= y return 1 otherwise return 0
     */
    private int thetaHelper(BigInteger xA, BigInteger xB, BigInteger yA, BigInteger yB){
        int result = (xA.add(xB).mod((BigInteger.TWO).pow(bitSize))).compareTo(yA.add(yB).mod((BigInteger.TWO).pow(bitSize)));
        if(result >=0){
            return 1;
        }
        else return 0;
    }

}
