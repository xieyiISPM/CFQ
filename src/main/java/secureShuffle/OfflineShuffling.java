package secureShuffle;

import com.n1analytics.paillier.PaillierPrivateKey;
import com.n1analytics.paillier.PaillierPublicKey;


import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * After initializing offlineShuffling object,
 * user has to genVArray,  genUArray and pass Pi to offlineShuffling object
 */
public class OfflineShuffling  {

    private BigInteger[] U; //only for test
    private BigInteger[] V; //only for test
    private int bitSize;
    private Integer[] pi;
    public OfflineShuffling(int bitSize){
        this.bitSize = bitSize;
    }

    public BigInteger[] genVArray(int arraySize){
        BigInteger[] vArray = genRandomArray(arraySize, bitSize);
        this.V = vArray;
        return vArray;
    }

    public BigInteger[] genUArray(int arraySize){
        BigInteger[] uArray = genRandomArray(arraySize, bitSize);
        this.U = uArray;
        return uArray;
    }

    public void addPi(Integer[] pi){
        this.pi = pi;
    }

    public Integer[] getPi(){
        return this.pi;
    }

    public BigInteger[] getU(){
        if(U == null){
            throw new NullPointerException();
        }
        else {
            return U;
        }
    }

    public BigInteger[] getV(){
        if (V==null){
            throw new NullPointerException();
        }
        else {
            return V;
        }
    }
    public int getBitSize(){
        return bitSize;
    }

    /**
     * B generates encrypted vector v: L0
     * @param arraySize size of v array and L0 array
     * @param bitSize   upper bound of v array is 2^bitsize -1
     * @param paillierPublicKey  Paillier public key
     * @return return L0 array
     */
    public BigInteger[] genL0(int arraySize, int bitSize, PaillierPublicKey paillierPublicKey){
        /*BigInteger[] vArray = genRandomArray(arraySize, bitSize);
        //System.out.println("v array:");
        //printList(vArray);
        V = vArray;*/
        if (V == null){
            throw new NullPointerException();
        }
        BigInteger L0[] = new BigInteger[arraySize];
        for(int i = 0; i< L0.length; i++){
            L0[i] = paillierPublicKey.raw_encrypt(V[i]);
        }
        //System.out.println("L0: ");
        //printList(L0);
        return L0;
    }

    /**
     * A generates L1
     * @param arraySize size of u array, r array, and L1 array
     * @param bitSize upper bounds of u array is 2^bitSize -1
     * @param twoToL A very big number which is at least bigger than 2* 2^bitSize to make sure u+v is smaller than twoToL
     * @param L0 L0 array from Hi
     * @param paillierPublicKey  Paillier public key
     * @return return L1 array
     */
    public BigInteger[] genL1(int arraySize, int bitSize, BigInteger twoToL, BigInteger[] L0, PaillierPublicKey paillierPublicKey){
        /*BigInteger[] uArray = genRandomArray(arraySize,bitSize);
        U = uArray;*/
        //System.out.println("u Array:");
        //printList(uArray);

        if (this.U == null){
            throw new NullPointerException();
        }
        BigInteger[] rArray = genRArray(arraySize,bitSize, twoToL);
        //System.out.println("r Array:");
        //printList(rArray);
        BigInteger L1[] = new BigInteger[arraySize];
        for(int i=0; i< arraySize; i++){
            BigInteger uPlusR = paillierPublicKey.raw_encrypt((U[i].add(rArray[i])).mod(twoToL));
            L1[i] = paillierPublicKey.raw_add(L0[i], uPlusR);
            //L1[i] = (L0[i].multiply(uPlusR)).mod(paillierPublicKey.getModulusSquared());
        }
        InitSet initSet = new InitSet();
        return initSet.permRandomArray(L1,this.pi);
    }

    /**
     * Generate offline output for B, which is L2 array
     * @param L1 L1 array from Medical Center C
     * @param twoToL A very big number which is at least bigger than 2* 2^bitSize to make sure u+v is smaller than twoToL
     * @param paillierPrivateKey Paillier private key
     * @return return Hi output [-u-v], which is permuted though permutation function pi
     */
    public BigInteger[] genL2(BigInteger[] L1,BigInteger twoToL, PaillierPrivateKey paillierPrivateKey){
        BigInteger[] L2 = new BigInteger[L1.length];

        for(int i= 0; i< L1.length; i++){
            L2[i] = paillierPrivateKey.raw_decrypt(L1[i]);
            L2[i] = (L2[i].mod(twoToL)).negate();
        }
        //System.out.println("L2");
        //printList(L2);
        return L2;
    }


    /**
     * Generate random array
     * @param arraySize array size
     * @param bitSize bitSize
     * @return BigInteger array
     */
    private BigInteger[] genRandomArray(int arraySize, int bitSize){
        BigInteger [] randArray = new BigInteger[arraySize];
        SecureRandom srand = new SecureRandom();

        for(int i = 0; i< arraySize; i++){
            randArray[i] = new BigInteger(bitSize, srand);
        }
        return randArray;
    }

    /**
     * Generate r array, twoToL must be carefully chosen,
     * @param arraySize r array's size
     * @param bitSize   r < 2^bitsize -1
     * @param twoToL  twoToL must larger than u + v: if u and v is smaller than 1024, then twoToL must be larger than 2048
     * @return
     */
    private BigInteger[] genRArray(int arraySize, int bitSize, BigInteger twoToL){
        BigInteger[] rArray = new BigInteger[arraySize];
        SecureRandom srand = new SecureRandom();
        for(int i = 0; i< arraySize; i++){
            BigInteger temp = new BigInteger(bitSize, srand);
            rArray[i] = temp.multiply(twoToL);
        }
        return rArray;
    }

    /**
     * mod function implementation
     * @param a number
     * @param b modular
     * @return
     */
    public int myMod(int a, int b){
        return (a%b + b)%b;
    }

    /**
     * Helper function print list
     * @param arr array list
     */
    private static void printList(BigInteger[] arr){
        for(int i = 0; i< arr.length;i++){
            System.out.print(arr[i] + " " );
        }
        System.out.println();
    }
}
