import com.n1analytics.paillier.PaillierPrivateKey;
import com.n1analytics.paillier.PaillierPublicKey;
import org.junit.jupiter.api.Test;
import secureShuffle.InitSet;
import secureShuffle.OfflineShuffling;
import secureShuffle.OnlineShuffling;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

public class ShufflingTest {
    @Test
    void testShuffling() throws Exception{
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
        //offlineTest();
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

    /**
     * test mymod function
     * @param testRound Test times
     * @return
     */
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

    /**
     * Test offline shuffling. Warning: bitsize < Paillier secure factor/2
     * for example if Paillier keys are genereated by 1024. the bitsize should less than 512
     */
    @Test
    void offlineTest(){
        OfflineShuffling offlineShuffling = new OfflineShuffling();
        int arraySize = 1000;
        int bitSize = 10;

        BigInteger twoToL = (BigInteger.TWO).pow(bitSize);
        PaillierPrivateKey paillierPrivateKey = PaillierPrivateKey.create(1024);
        PaillierPublicKey paillierPublicKey = paillierPrivateKey.getPublicKey();
        boolean testResult = true;
        for(int j= 0; j< 1; j++) { //test 100 times
            InitSet initSet = new InitSet();
            Integer[] pi = initSet.genPi(arraySize);
            BigInteger[] L0 = offlineShuffling.genL0(arraySize, bitSize, paillierPublicKey);
            BigInteger[] L1 = offlineShuffling.genL1(arraySize, bitSize, twoToL, L0, pi, paillierPublicKey);
            BigInteger[] L2 = offlineShuffling.genL2(L1, twoToL, paillierPrivateKey);

            LinkedList<BigInteger> uvList = new LinkedList<>();
            for (int i = 0; i < arraySize; i++) {
                uvList.add((offlineShuffling.U[i].add(offlineShuffling.V[i])).mod(twoToL));
            }

            System.out.println("U list:");
            printList(offlineShuffling.U);

            System.out.println("V list:");
            printList(offlineShuffling.V);

            System.out.println("L2 list: ");
            printList(L2);

            System.out.println("UV list");
            Iterator<BigInteger> it = uvList.iterator();
            while(it.hasNext()){
                System.out.print(it.next() + " ");
            }
            System.out.println();

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

    /**
     * Test online shuffling with offline result. Warning: bitsize < Paillier secure factor/2
     * for example if Paillier keys are genereated by 1024. the bitsize should less than 512
     * @return test result
     * @throws Exception
     */
    @Test
    String onlineShufflingTest() throws Exception{
        OfflineShuffling offlineShuffling = new OfflineShuffling();
        int arraySize = 1000;
        int bitSize = 100; // note  bitsize < paillier secrete factor/2
        BigInteger twoToL = (BigInteger.TWO).pow(bitSize);
        PaillierPrivateKey paillierPrivateKey = PaillierPrivateKey.create(1024);
        PaillierPublicKey paillierPublicKey = paillierPrivateKey.getPublicKey();
        InitSet initSet = new InitSet();
        Integer[] pi = initSet.genPi(arraySize);
        BigInteger[] L0 = offlineShuffling.genL0(arraySize, bitSize, paillierPublicKey);
        BigInteger[] L1 = offlineShuffling.genL1(arraySize, bitSize, twoToL, L0, pi, paillierPublicKey);
        BigInteger[] L2 = offlineShuffling.genL2(L1, twoToL, paillierPrivateKey);

        OnlineShuffling onlineShuffling = new OnlineShuffling();
        BigInteger[] xB = initSet.genRandomArray(arraySize, bitSize);
        BigInteger[] xA = initSet.genRandomArray(arraySize, bitSize);

        BigInteger[] L3 = onlineShuffling.genL3(offlineShuffling.V, xB, twoToL);
        BigInteger[] L4 = onlineShuffling.genL4(offlineShuffling.U,L3,xA, twoToL, pi);

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
        /*System.out.println("Secret Array before sort:");
        printList(secretArray);*/
        //Arrays.parallelSort(secretArray);

        /*System.out.println("Secret Array after sort:");
        printList(secretArray);*/

        BigInteger[] xList = new BigInteger[arraySize];
        for (int i = 0; i < arraySize; i++) {
            xList[i]=xB[i].add(xA[i]).mod(twoToL);
        }

        BigInteger[] xShuffledList = reorderArray(xList, pi);


        /*System.out.println("xList Array before sort:");
        printList(xList);*/

        //Arrays.parallelSort(xList);

        /*System.out.println("xList Array after sort:");
        printList(xList);*/

        for (int i = 0; i < arraySize; i++) {

            if (secretArray[i].compareTo(xShuffledList[i])!=0) {
                System.out.println(xShuffledList[i]);
                return "online Test fails!";
            }
        }
        return "online Test passes!";
    }

    /**
     * Test BigInteger's hascode
     * @return
     */
    @Test
     boolean hashCodeTest(){
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

    private static BigInteger[] reorderArray(BigInteger[] arr, Integer[] pi){
        BigInteger[] reordered = new BigInteger[arr.length];
        for(int i= 0; i< reordered.length; i++){
            reordered[i] = arr[pi[i]];
        }
        return reordered;
    }


}
