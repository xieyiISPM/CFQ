import com.n1analytics.paillier.PaillierPrivateKey;
import com.n1analytics.paillier.PaillierPublicKey;
import secureShuffle.OfflineShuffling;

import java.math.BigInteger;
import java.util.*;

public class OfflineShufflingTest {
    public static void main(String[] args){
        OfflineShuffling offlineShuffling = new OfflineShuffling();
        /*BigInteger[] randomArray = offlineShuffling.genRandomArray(5,128);
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
        System.out.println();*/

        //System.out.println(modTest(1000));
        offlineTest();
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

    private static boolean modTest(int testRound){
        Random rnd = new Random();
        boolean result = true;


        OfflineShuffling offlineShuffling =new OfflineShuffling();
        for(int i = 0; i< testRound; i++) {
            int a = rnd.nextInt(1024);
            int b = rnd.nextInt(2048) + 1;
            int c = offlineShuffling.myMod(a, b);
           // System.out.println("Mymod result:" + a + " % " + b + "=" + c);

            BigInteger bigA = BigInteger.valueOf(a);
            BigInteger bigB = BigInteger.valueOf(b);
            BigInteger bigc = bigA.mod(bigB);
            //System.out.println("BigInteger reulst: " + bigc);
            if (!bigc.toString().equals(BigInteger.valueOf(c).toString())) {
                result = false;
                break;
            }
        }
        return result;

    }

    private static void offlineTest(){
        OfflineShuffling offlineShuffling = new OfflineShuffling();
        int arraySize = 100;
        int bitSize = 10;

        BigInteger twoToL = BigInteger.valueOf(2^(2*2024));
        PaillierPrivateKey paillierPrivateKey = PaillierPrivateKey.create(1024);
        PaillierPublicKey paillierPublicKey = paillierPrivateKey.getPublicKey();
        boolean testResult = true;
        for(int j= 0; j< 500; j++) {
            int[] pi = offlineShuffling.genPi(arraySize);
            BigInteger[] L0 = offlineShuffling.genL0(arraySize, bitSize, paillierPublicKey);
            BigInteger[] L1 = offlineShuffling.genL1(arraySize, bitSize, twoToL, L0, pi, paillierPublicKey);
            BigInteger[] L2 = offlineShuffling.genL2(L1, twoToL, paillierPrivateKey);

            LinkedList<BigInteger> uvList = new LinkedList<>();
            for (int i = 0; i < arraySize; i++) {
                uvList.add(offlineShuffling.U[i].add(offlineShuffling.V[i]));
            }

            Iterator<BigInteger> it = uvList.iterator();
            while(it.hasNext()){
                System.out.print(it.next() + " ");
            }
            System.out.println();

            for (int i = 0; i < arraySize; i++) {
                boolean isRemoved = uvList.remove(L2[i].negate());
                if (!isRemoved) {
                    System.out.println("Test fails!");
                    System.out.println(L2[i].negate());
                    return;
                }
            }
            if (!uvList.isEmpty()) {
                testResult = false;
                System.out.println("Test fails!");
                return;
            }
        }
        if(testResult){
            System.out.println("Test passed!");
        }
    }


}
