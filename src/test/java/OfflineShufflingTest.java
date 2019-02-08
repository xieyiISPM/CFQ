import com.n1analytics.paillier.PaillierPrivateKey;
import com.n1analytics.paillier.PaillierPublicKey;
import secureShuffle.OfflineShuffling;

import java.math.BigInteger;

public class OfflineShufflingTest {
    public static void main(String[] args){
        OfflineShuffling offlineShuffling = new OfflineShuffling();
        BigInteger[] randomArray = offlineShuffling.genRandomArray(10,100);
        printList(randomArray);

        int[] perm = offlineShuffling.genPi(10);
        printList(perm);

        BigInteger[] permRand = offlineShuffling.permRandomArray(randomArray,perm);
        printList(permRand);

        PaillierPrivateKey sk = PaillierPrivateKey.create(1024);
        PaillierPublicKey pk = sk.getPublicKey();

        BigInteger[] L0 = offlineShuffling.genL0(permRand,pk);
        printList(L0);

        for(int i= 0; i< L0.length; i++){
            System.out.print(sk.raw_decrypt(L0[i]) + " ");
        }
        System.out.println();
    }

    private static void printList(BigInteger[] arr){
        for(int i = 0; i< arr.length;i++){
            System.out.print(arr[i] + " " );
        }
        System.out.println();
    }

    private static void printList(int[] arr){
        for(int i = 0; i< arr.length;i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }


}
