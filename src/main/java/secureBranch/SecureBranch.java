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

    public void addAndCompare(BigInteger[] xA, BigInteger[] xB, BigInteger[] yA, BigInteger[] yB) throws Exception{
        if(xA.length != xB.length || yA.length!= yB.length || xA.length != yA.length || xA.length != 2){
            throw new IllegalArgumentException("Array sizes do not match!");
        }

        int arraySizeX = xA.length;

        SSF ssf = new SSF(bitSize);
        int[] pi = ssf.getPi(arraySizeX);
        OfflineShuffling offlineShufflingX = new OfflineShuffling();
        BigInteger[] xHPrime = ssf.getOfflineOutput(arraySizeX, offlineShufflingX, pi);
        BigInteger[] xCPrime = ssf.getOnlineOuptut(arraySizeX,xA, xB,offlineShufflingX, pi );

        printArr(xHPrime,"xHPrime");
        printArr(xCPrime,"xCPrime");

        System.out.println("X0Prime reconstruct: " + reconstruct(xHPrime[0], xCPrime[0], bitSize));
        System.out.println("X1Prime reconstruct: " + reconstruct(xHPrime[1], xCPrime[1], bitSize));
        System.out.println();



        OfflineShuffling offlineShufflingY = new OfflineShuffling();
        BigInteger[] yHPrime = ssf.getOfflineOutput(arraySizeX, offlineShufflingY,pi);
        BigInteger[] yCPrime = ssf.getOnlineOuptut(arraySizeX,yA, yB,offlineShufflingY,pi );

        printArr(yHPrime,"yHPrime");
        printArr(yCPrime,"yCPrime");

        System.out.println("y0Prime reconstruct: " + reconstruct(yHPrime[0], yCPrime[0], bitSize));
        System.out.println("y1Prime reconstruct: " + reconstruct(yHPrime[1], yCPrime[1], bitSize));
        System.out.println();


       // GarbledCircuit addcmpGC = new GarbledCircuit("ADD-CMP.cir", "b-input", "a-input", "GCParser/results/siclientout", "GCParser/results/siserverout");
        //int theta = addcmpGC.GCADDCMPOutPut(xHPrime[0], xCPrime[0], xHPrime[1], xCPrime[1]);

        int theta = thetaHelper(xHPrime[0], xCPrime[0], xHPrime[1], xCPrime[1]);
        //check which should be assigned to yOutputA/yOutputB
        System.out.println("theta = "+ theta);
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

    private void printArr(BigInteger[] arr, String varName){

        int i = 0;
        for(BigInteger bi: arr){
            System.out.println(varName + " " + i + ": " + bi);
            i++;
        }
        System.out.println();
    }

    private  BigInteger reconstruct(BigInteger a, BigInteger b, int bitSize){
        BigInteger m = (BigInteger.TWO).pow(bitSize);
        BigInteger sum = (a.add(b)).mod(m);
        return sum;
    }

    private int thetaHelper(BigInteger xA, BigInteger xB, BigInteger yA, BigInteger yB){
        int result = (xA.add(xB).mod((BigInteger.TWO).pow(bitSize))).compareTo(yA.add(yB).mod((BigInteger.TWO).pow(bitSize)));
        if(result >0){
            return 1;
        }
        else return 0;
    }




}
