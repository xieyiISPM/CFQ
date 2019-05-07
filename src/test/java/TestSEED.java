import org.junit.jupiter.api.Test;
import secureExactEditDistance.SecureExactEditDistance;
import secureShuffle.InitSet;
import secureShuffle.OfflineShuffling;
import secureShuffle.OfflineShufflingPool;

import java.math.BigInteger;

public class TestSEED {
    @Test
    void testSEED() throws Exception{
        int arraySize = 23;
        int bitSize = 5; // note  bitsize < paillier secrete factor/2
        InitSet initSet = new InitSet();
        BigInteger[] xB = initSet.genRandomArray(arraySize, bitSize);
        BigInteger[] xA = initSet.genRandomArray(arraySize, bitSize);

        BigInteger[] yB = initSet.genRandomArray(arraySize, bitSize);
        BigInteger[] yA = initSet.genRandomArray(arraySize, bitSize);

        BigInteger twoToL = (BigInteger.TWO).pow(bitSize);

        System.out.print("xA: ");
        printArr(xA);
        System.out.print("xB: ");
        printArr(xB);

        System.out.print("x: ");
        printArr(reconstruct(xA,xB,twoToL));
        System.out.println();

        System.out.print("yA: ");
        printArr(yA);
        System.out.print("yB: ");
        printArr(yB);

        System.out.print("y: ");
        printArr(reconstruct(yA,yB,twoToL));
        System.out.println();

        SecureExactEditDistance seed = new SecureExactEditDistance(bitSize);
        OfflineShufflingPool pool = new OfflineShufflingPool(bitSize, new OfflineShuffling());
        pool.addToPool(arraySize);
        seed.setDistance(xA, xB, yA, yB, pool );

        System.out.println();
        System.out.println("dED_A = "+seed.getDedA());
        System.out.println("dED_B = "+seed.getDedB());
        System.out.println("reconstructed dED = " + seed.getDedA().add(seed.getDedB()).mod(twoToL));

    }

    private void printArr(BigInteger[] arr){
        for (BigInteger item: arr){
            System.out.print(item + ", ");
        }
        System.out.println();
    }

    private BigInteger[] reconstruct(BigInteger[] xA, BigInteger[] xB, BigInteger modular){
        if(xA.length != xB.length){
            throw new IndexOutOfBoundsException();
        }
        BigInteger[] sum = new BigInteger[xA.length];

        for (int i = 0; i< xA.length;i++){
            sum[i] = (xA[i].add(xB[i])).mod(modular);
        }
        return sum;
    }


}
