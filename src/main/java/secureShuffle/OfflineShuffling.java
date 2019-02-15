package secureShuffle;

import com.n1analytics.paillier.PaillierPrivateKey;
import com.n1analytics.paillier.PaillierPublicKey;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;

import java.math.BigInteger;
import java.security.SecureRandom;

public class OfflineShuffling <T> {
    private int  bitSize = 1024;
    private int n = 2;
    public BigInteger[] U;
    public BigInteger[] V;
    public OfflineShuffling(){

    }
    public OfflineShuffling(int bitSize, int n){
        this.bitSize = bitSize;
        this.n = n;
    }

    public BigInteger[] genL0(int arraySize, int bitSize, PaillierPublicKey paillierPublicKey){
        BigInteger[] vArray = genRandomArray(arraySize, bitSize);
        //System.out.println("v array:");
        //printList(vArray);
        V = vArray;
        BigInteger L0[] = new BigInteger[arraySize];
        for(int i = 0; i< L0.length; i++){
            L0[i] = paillierPublicKey.raw_encrypt(vArray[i]);
        }
        //System.out.println("L0: ");
        //printList(L0);
        return L0;
    }

    public BigInteger[] genL1(int arraySize,int bitSize, BigInteger twoToL,BigInteger[] L0, int[] pi,PaillierPublicKey paillierPublicKey ){
        BigInteger[] uArray = genRandomArray(arraySize,bitSize);
        U = uArray;
        //System.out.println("u Array:");
        //printList(uArray);
        BigInteger[] rArray = genRArray(arraySize,bitSize, twoToL);
        //System.out.println("r Array:");
        //printList(rArray);
        BigInteger L1[] = new BigInteger[arraySize];
        for(int i=0; i< arraySize; i++){
            BigInteger uPlusR = paillierPublicKey.raw_encrypt(uArray[i].add(rArray[i]));
            L1[i] = L0[i].multiply(uPlusR);
        }
        return permRandomArray(L1,pi);
    }

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
     * Generate pi function : Create a random permutation of the given size;
     * @param size array size
     * @return permuted array index
     */
    public int[] genPi(int size){
        Permutation perm = new Permutation(size, new SecureRandom());
        return perm.getVector();
    }

    /**
     * Permutate random array by using pre-generated pi function
     * @param arr
     * @param pi
     * @return return permuted array
     */
    private BigInteger[] permRandomArray(BigInteger[] arr, int[] pi){
        if(arr.length != pi.length) {
            System.err.println("Array size does not match permutation function size");
            return null;
        }
        BigInteger[] permRand = new BigInteger[arr.length];
        for(int i = 0; i< arr.length; i++){
            permRand[i] = arr[pi[i]];
        }
        return permRand;
    }

    public int myMod(int a, int b){
        return (a%b + b)%b;
    }

    private static void printList(BigInteger[] arr){
        for(int i = 0; i< arr.length;i++){
            System.out.print(arr[i] + " " );
        }
        System.out.println();
    }
}
