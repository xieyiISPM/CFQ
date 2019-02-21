import com.n1analytics.paillier.PaillierPrivateKey;
import com.n1analytics.paillier.PaillierPublicKey;
import secureShuffle.InitSet;
import secureShuffle.OfflineShuffling;
import secureShuffle.OnlineShuffling;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

public class ShufflingTest {
    public static void main(String[] args) throws Exception{
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
        // offlineTest();
        //hashCodeTest();
        System.out.println(onlineShufflingTest());
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
        for(int j= 0; j< 100; j++) { //test 10 times
            InitSet initSet = new InitSet();
            int[] pi = initSet.genPi(arraySize);
            BigInteger[] L0 = offlineShuffling.genL0(arraySize, bitSize, paillierPublicKey);
            BigInteger[] L1 = offlineShuffling.genL1(arraySize, bitSize, twoToL, L0, pi, paillierPublicKey);
            BigInteger[] L2 = offlineShuffling.genL2(L1, twoToL, paillierPrivateKey);

            LinkedList<BigInteger> uvList = new LinkedList<>();
            for (int i = 0; i < arraySize; i++) {
                uvList.add((offlineShuffling.U[i].add(offlineShuffling.V[i])).mod(twoToL));
            }

            /*Iterator<BigInteger> it = uvList.iterator();
            while(it.hasNext()){
                System.out.print(it.next() + " ");
            }
            System.out.println();*/

            for (int i = 0; i < arraySize; i++) {
                boolean isRemoved = uvList.remove(L2[i].negate());
                if (!isRemoved) {
                    System.out.println("Offline Test fails!");
                    System.out.println(L2[i].negate());
                    return;
                }
            }
            if (!uvList.isEmpty()) {
                testResult = false;
                System.out.println("Offline Test fails!");
                return;
            }
        }
        if(testResult){
            System.out.println("Offline Test passed!");
        }
    }

    private static String onlineShufflingTest() throws Exception{
        OfflineShuffling offlineShuffling = new OfflineShuffling();
        int arraySize = 10;
        int bitSize = 10;
        BigInteger twoToL = BigInteger.valueOf(2^(2*2024));
        PaillierPrivateKey paillierPrivateKey = PaillierPrivateKey.create(1024);
        PaillierPublicKey paillierPublicKey = paillierPrivateKey.getPublicKey();
        InitSet initSet = new InitSet();
        int[] pi = initSet.genPi(arraySize);
        BigInteger[] L0 = offlineShuffling.genL0(arraySize, bitSize, paillierPublicKey);
        BigInteger[] L1 = offlineShuffling.genL1(arraySize, bitSize, twoToL, L0, pi, paillierPublicKey);
        BigInteger[] L2 = offlineShuffling.genL2(L1, twoToL, paillierPrivateKey);

        OnlineShuffling onlineShuffling = new OnlineShuffling();
        BigInteger[] xH = initSet.genRandomArray(arraySize, bitSize);
        BigInteger[] xC = initSet.genRandomArray(arraySize, bitSize);

        BigInteger[] L3 = onlineShuffling.genL3(offlineShuffling.V, xH, twoToL);
        BigInteger[] L4 = onlineShuffling.genL4(offlineShuffling.U,L3,xC, twoToL, pi);

        BigInteger[] secretArray = new BigInteger[arraySize];


        for(int i= 0; i< arraySize; i++){
            secretArray[i] = (L2[i].add(L4[i])).mod(twoToL);
        }

        // System.out.println("xH:");
        //printList(xH);
        //System.out.println("xC");
        //printList(xC);
        //System.out.println("u:");
        //printList(offlineShuffling.U);
        //System.out.println("v:");
        //printList(offlineShuffling.V);
        // System.out.println("L3=xH + v");
        //printList(L3);
        //System.out.println("L4 = x + u + v (shuffled)");
        //printList(L4);
        //System.out.println("L2=-u-v (shuffled!)");
        //printList(L2);
        System.out.println("Secret Array");
        printList(secretArray);

        LinkedList<BigInteger> xList = new LinkedList<>();
        for (int i = 0; i < arraySize; i++) {
            xList.add(xH[i].add(xC[i]));
        }

        System.out.println("xList");
        for(int i= 0; i< xList.size(); i++){
            System.out.print(xList.get(i) + " ");
        }
        System.out.println();


        for (int i = 0; i < arraySize; i++) {
            boolean isRemoved = xList.remove(secretArray[i]);
            if (!isRemoved) {
                System.out.println(xList.get(i));
                return "online Test fails!";

            }
        }
        if (!xList.isEmpty()) {
            return "online Test fails!";
        }
        else {
            return "onLine Test passed!";
        }





    }

    private static boolean hashCodeTest(){
        HashSet<BigInteger> hashSet = new HashSet<>();
        for(int i= 0; i< 100000; i++){
            Random rnd = new SecureRandom();
            int a = rnd.nextInt(100000);
            int b = rnd.nextInt(100000);
            int c = a + b;
            hashSet.add(BigInteger.valueOf(c));
            BigInteger bigA = BigInteger.valueOf(a);
            BigInteger bigB = BigInteger.valueOf(b);
            BigInteger bigC = bigA.add(bigB);

            if(!hashSet.remove(bigC)){
                System.out.println("Remove from hash set!");
                return false;
            }
        }
        if(hashSet.isEmpty()) {
            System.out.println("hashcode Test passed!");
            return true;
        }
        else{
            System.out.println("hashCode Test fails!");
            return false;
        }
    }


}
