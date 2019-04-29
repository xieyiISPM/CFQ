package secureBranch;

import gc.GarbledCircuit;
import secureShuffle.OfflineShuffling;
import secureShuffle.SSF;

import java.math.BigInteger;
import java.util.Arrays;

public class SecureBranch {
    private int bitSize = 10;
    private BigInteger yOutputA = null;
    private BigInteger yOutputB = null;


    public SecureBranch(int bitSize){
        this.bitSize = bitSize;
    }

    /**
     *  Generate outputs yA for A, and yB for B. It must be run before get yA and yB
     * @param xA A's x share
     * @param xB B's x share
     * @param yA A's y share
     * @param yB B's y share
     * @throws Exception
     */
    public void addAndCompare(BigInteger[] xA, BigInteger[] xB, BigInteger[] yA, BigInteger[] yB) throws Exception{
        if(xA.length != xB.length || yA.length!= yB.length || xA.length != yA.length || xA.length != 2){
            throw new IllegalArgumentException("Array sizes do not match!");
        }

        int arraySizeX = xA.length;

        SSF ssf = new SSF(bitSize);
        int[] pi = ssf.getPi(arraySizeX);

        OfflineShuffling offlineShufflingX = new OfflineShuffling();
        BigInteger[] xBPrime = ssf.getOfflineOutput(arraySizeX, offlineShufflingX, pi);
        BigInteger[] xAPrime = ssf.getOnlineOuptut(arraySizeX,xB, xA,offlineShufflingX, pi );

        /*System.out.println("shuffle pi: " + Arrays.toString(pi));
        printArr(xBPrime,"xHPrime");
        printArr(xAPrime,"xCPrime");

        System.out.println("X0Prime reconstruct: " + reconstruct(xBPrime[0], xAPrime[0], bitSize));
        System.out.println("X1Prime reconstruct: " + reconstruct(xBPrime[1], xAPrime[1], bitSize));
        System.out.println();
*/


        OfflineShuffling offlineShufflingY = new OfflineShuffling();
        BigInteger[] yBPrime = ssf.getOfflineOutput(arraySizeX, offlineShufflingY,pi);
        BigInteger[] yAPrime = ssf.getOnlineOuptut(arraySizeX,yB, yA,offlineShufflingY,pi );

        /*printArr(yAPrime,"yAPrime");
        printArr(yBPrime,"yBPrime");

        System.out.println("y0Prime reconstruct: " + reconstruct(yBPrime[0], yAPrime[0], bitSize));
        System.out.println("y1Prime reconstruct: " + reconstruct(yBPrime[1], yAPrime[1], bitSize));
        System.out.println();
*/

       // GarbledCircuit addcmpGC = new GarbledCircuit("ADD-CMP.cir", "b-input", "a-input", "GCParser/results/siclientout", "GCParser/results/siserverout");
        //int theta = addcmpGC.GCADDCMPOutPut(xHPrime[0], xCPrime[0], xHPrime[1], xCPrime[1]);

        int theta = thetaHelper(xBPrime[0], xAPrime[0], xBPrime[1], xAPrime[1]);

        // System.out.println("theta = "+ theta);
        if(theta == 1){
            yOutputA = yAPrime[0];
            yOutputB = yBPrime[0];

        }
        else{
            yOutputA = yAPrime[1];
            yOutputB = yBPrime[1];
        }

    }

    /**
     * Heper method: combine BigIntegers  to a array array
     * @param bigInt1
     * @param bigInt2
     * @return
     */
    public BigInteger[] genArray(BigInteger bigInt1, BigInteger bigInt2){
        BigInteger[] arr= new BigInteger[2];
        arr[0] = bigInt1;
        arr[1] = bigInt2;
        return arr;

    }

    /**
     * get YA
     * @return
     */
    public BigInteger getYA(){
        return yOutputA;
    }

    /**
     * get YB
     * @return
     */
    public BigInteger getYB(){
        return yOutputB;
    }

    /**
     * hepler function to print BigInteger array
     * @param arr
     * @param varName
     */
    private void printArr(BigInteger[] arr, String varName){

        int i = 0;
        for(BigInteger bi: arr){
            System.out.println(varName + " " + i + ": " + bi);
            i++;
        }
        System.out.println();
    }

    /**
     * Helper function: reconstruct two shares
     * @param a share 1
     * @param b share 2
     * @param bitSize
     * @return reconstructed original value
     */
    private  BigInteger reconstruct(BigInteger a, BigInteger b, int bitSize){
        BigInteger m = (BigInteger.TWO).pow(bitSize);
        BigInteger sum = (a.add(b)).mod(m);
        return sum;
    }

    /**
     * This is a cheating method to avoid using GC, which is time consuming.
     * @param x0A add-compare circuit input 1 from A
     * @param x0B add-compare circuit input 1 from B
     * @param x1A add-compare circuit input 2 from A
     * @param x1B add-compare circuit input 2 from B
     * @return if x0 >= x1 return 1 else return 0
     */
    private int thetaHelper(BigInteger x0A, BigInteger x0B, BigInteger x1A, BigInteger x1B){
        int result = (x0A.add(x0B).mod((BigInteger.TWO).pow(bitSize))).compareTo(x1A.add(x1B).mod((BigInteger.TWO).pow(bitSize)));
        if(result >=0){
            return 1;
        }
        else return 0;
    }




}
