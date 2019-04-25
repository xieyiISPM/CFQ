package helper;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

public class Helper <T> {
    private int bitSize;
    private BigInteger twoToL;
    private Triple<BigInteger[], BigInteger[], BigInteger[]> query;
    private Triple<BigInteger[][], BigInteger[][], BigInteger[][]> GS;

    public Helper(int bitSize){
        this.bitSize = bitSize;
        this.twoToL = BigInteger.TWO.pow(bitSize);
    }


    /**
     * Generate random shared number < share1, original-number, share2>
     * @param bitSize
     * @param arraySize
     * @return
     */
    private ArrayList<Triple<BigInteger, BigInteger,BigInteger>> genShares(int bitSize, int arraySize) {
        SecureRandom srand = new SecureRandom();
        BigInteger[] arr = new BigInteger[2];
        BigInteger m = this.twoToL;

        ArrayList<Triple<BigInteger, BigInteger, BigInteger>> bigTripleArray = new ArrayList<>();
        for(int i=0; i< arraySize; i++){
            BigInteger sum = new BigInteger(bitSize, srand);
            arr[0] = new BigInteger(bitSize, srand);
            arr[1] = (sum.subtract(arr[0])).mod(m);
            bigTripleArray.add(new ImmutableTriple(arr[0], sum, arr[1]));
        }
        return bigTripleArray;
    }

    public void genQuery(int arraySize){
        ArrayList<Triple<BigInteger, BigInteger,BigInteger>> bigTripleQShare = genShares(bitSize, arraySize);
        BigInteger[] QA = new BigInteger[arraySize];
        BigInteger[] QB = new BigInteger[arraySize];
        BigInteger[] Q = new BigInteger[arraySize];

        for(int i=0; i< arraySize;i++ ){
            QA[i]= bigTripleQShare.get(i).getLeft();
            QB[i]= bigTripleQShare.get(i).getRight();
            Q[i]= bigTripleQShare.get(i).getMiddle();
        }
        this.query= new ImmutableTriple<>(QA,Q,QB);
        /*System.out.print("Original: ");
        printArr(Q);
        System.out.print("QA: ");
        printArr(QA);
        System.out.print("QB: ");
        printArr(QB);*/
    }

    public void genGS(int arraySize, int records){
        BigInteger[][] SA = new BigInteger[records][arraySize];
        BigInteger[][] SB = new BigInteger[records][arraySize];
        BigInteger[][] S = new BigInteger[records][arraySize];

        for(int i= 0; i< records; i++){
            ArrayList<Triple<BigInteger, BigInteger,BigInteger>> bigTripleSShare = genShares(bitSize, arraySize);

            for(int j=0; j< arraySize;j++){
                SA[i][j] = bigTripleSShare.get(j).getLeft();
                S[i][j] = bigTripleSShare.get(j).getMiddle();
                SB[i][j] = bigTripleSShare.get(j).getRight();
            }
        }

        this.GS = new ImmutableTriple<>(SA,S, SB);

    }

    public BigInteger[][] getGSA(){
        if(GS!= null){
            return GS.getLeft();
        }
        else{
            throw new IndexOutOfBoundsException();
        }
    }

    public BigInteger[][] getGSB(){
        if(GS!= null){
            return GS.getRight();
        }
        else{
            throw new IndexOutOfBoundsException();
        }
    }

    public BigInteger[][] getOriginalGS(){
        if(GS!= null){
            return GS.getMiddle();
        }
        else{
            throw new IndexOutOfBoundsException();
        }
    }


    public BigInteger[] getQueryA(){
        if(query != null){
            return query.getLeft();
        }
        else{
            throw new IndexOutOfBoundsException();
        }
    }

    public BigInteger[] getQueryB(){
        if(query != null){
            return query.getRight();
        }
        else{
            throw new IndexOutOfBoundsException();
        }
    }

    public BigInteger[] getOriginalQuery(){
        if(query != null){
            return query.getMiddle();
        }
        else{
            throw new IndexOutOfBoundsException();
        }
    }



    public void printArr(BigInteger[] arr){

        for(BigInteger bi: arr){
            System.out.print( bi + " ");
        }
        System.out.println();
    }

    public BigInteger reconstruct(BigInteger bigA, BigInteger bigB, int bitSize){
        BigInteger twoToL = BigInteger.TWO.pow(bitSize);
        return bigA.add(bigB).mod(twoToL);
    }

    public void GSRecordsPrinter(T[][] GS){
        if(GS==null){
            System.out.println("null records!");
        }
        for(T[] record: GS ){
            printArr((BigInteger[])record);
        }

    }

    public void GSOriginalPrinter(T[][] GS){
        if(GS==null){
            System.out.println("null records!");
        }
        for(T[] record: GS ){
            printArr((BigInteger[])record);
        }

    }

    public void printSingleIndexDistancePair(Pair<T, T> indexDistancePair){
        System.out.print(indexDistancePair.getLeft() + " " + indexDistancePair.getRight());
    }

    public void printAllIndexDistancePair(Pair<T,T>[] indexDistanceArr){
        System.out.println("Index:");
        for(Pair<T, T> indexDistancePair : indexDistanceArr){
            System.out.print(indexDistancePair.getLeft() + " ");
        }
        System.out.println();
        System.out.println("Distance:");
        for(Pair<T, T> indexDistancePair : indexDistanceArr){
            System.out.print(indexDistancePair.getRight() + " ");
        }
        System.out.println();
    }
}
