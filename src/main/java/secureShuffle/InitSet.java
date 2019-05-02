package secureShuffle;

import org.bouncycastle.pqc.math.linearalgebra.Permutation;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public class InitSet {
    public InitSet(){

    }

    /**
     * Generate pi function : Create a random permutation of the given size;
     * @param size array size
     * @return permuted array index
     */
    public Integer[] genPi(int size){
        Permutation perm = new Permutation(size, new SecureRandom());
        int[] temp = perm.getVector();
        return Arrays.stream(temp).boxed().toArray( Integer[]::new );
    }

    /**
     * Permutate random array by using pre-generated pi function
     * @param arr
     * @param pi
     * @return return permuted array
     */
    public BigInteger[] permRandomArray(BigInteger[] arr, Integer[] pi){
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

    public BigInteger[] genRandomArray(int arraySize, int bitSize) {
        BigInteger[] randArray = new BigInteger[arraySize];
        SecureRandom srand = new SecureRandom();

        for (int i = 0; i < arraySize; i++) {
            randArray[i] = new BigInteger(bitSize, srand);
        }
        return randArray;
    }


}
