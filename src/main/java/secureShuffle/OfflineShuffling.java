package secureShuffle;

import com.n1analytics.paillier.PaillierPublicKey;
import org.bouncycastle.pqc.math.linearalgebra.Permutation;

import java.math.BigInteger;
import java.security.SecureRandom;

public class OfflineShuffling <T> {
    private int l = 1024;
    private int n = 2;
    public OfflineShuffling(){

    }
    public OfflineShuffling(int l, int n){
        this.l = l;
        this.n = n;
    }

    public BigInteger[] genL0(BigInteger[] arr, PaillierPublicKey paillierPublicKey){
        BigInteger L0[] = new BigInteger[arr.length];
        for(int i = 0; i< L0.length; i++){
            L0[i] = paillierPublicKey.raw_encrypt(arr[i]);
        }
        return L0;
    }


    /**
     * Generate random array
     * @param size array size
     * @param max upperbound
     * @return BigInteger array
     */
    public BigInteger[] genRandomArray(int size, int max){
        BigInteger [] randArray = new BigInteger[size];
        for(int i = 0; i< size; i++){
            SecureRandom rand = new SecureRandom();
            randArray[i] = BigInteger.valueOf(rand.nextInt(max));
        }
        return randArray;
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
    public BigInteger[] permRandomArray(BigInteger[] arr, int[] pi){
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
}
