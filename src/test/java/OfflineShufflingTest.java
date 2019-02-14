import com.n1analytics.paillier.PaillierPrivateKey;
import com.n1analytics.paillier.PaillierPublicKey;
import secureShuffle.OfflineShuffling;

import java.math.BigInteger;

public class OfflineShufflingTest {
    public static void main(String[] args){
        OfflineShuffling offlineShuffling = new OfflineShuffling();
        BigInteger[] randomArray = offlineShuffling.genRandomArray(5,128);
        printList(randomArray);

        int[] perm = offlineShuffling.genPi(5);
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

            System.out.println(modTest(11, 12556));
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

    private static boolean modTest(int a, int b){
        OfflineShuffling offlineShuffling =new OfflineShuffling();
        int c = offlineShuffling.myMod(a, b);
        System.out.println("Mymod result:"+ a + " % " + b + "=" + c);

        BigInteger bigA = BigInteger.valueOf(a);
        BigInteger bigB = BigInteger.valueOf(b);
        BigInteger bigc = bigA.mod(bigB);
        System.out.println("BigInteger reulst: " + bigc);
        if(bigc.toString().equals(BigInteger.valueOf(c).toString())){
            return true;
        }
        else {
            return false;}

    }


}
