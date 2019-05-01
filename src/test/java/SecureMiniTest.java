import org.junit.jupiter.api.Test;
import secureMinimumSelection.SecureMiniSelection;
import secureShuffle.InitSet;

import java.math.BigInteger;

public class SecureMiniTest {
    /*public static void main(String[] args) throws Exception{
        int arraySize = 10;
        int bitSize = 5; // note  bitsize < paillier secrete factor/2
        InitSet initSet = new InitSet();
        BigInteger[] xH = initSet.genRandomArray(arraySize, bitSize);
        BigInteger[] xC = initSet.genRandomArray(arraySize, bitSize);
        secureMiniTest(xC,xH, bitSize);


    }*/

    //private static void secureMiniTest(BigInteger[] xA, BigInteger[] xB, int bitSize) throws Exception{
    @Test
    void testSecureMini() throws Exception{
        int arraySize = 2000;
        int bitSize = 10; // note  bitsize < paillier secrete factor/2
        InitSet initSet = new InitSet();
        BigInteger[] xB = initSet.genRandomArray(arraySize, bitSize);
        BigInteger[] xA = initSet.genRandomArray(arraySize, bitSize);
        BigInteger twoToL = (BigInteger.TWO).pow(bitSize);

        System.out.print("xA: ");
        printArr(xA);
        System.out.print("xB: ");
        printArr(xB);

        System.out.print("x: ");
        printArr(reconstruct(xA,xB,twoToL));
        System.out.println();

        SecureMiniSelection sm = new SecureMiniSelection(bitSize);
        sm.getMini(xA, xB);
        System.out.println();
        System.out.println("mininum A = "+sm.getMinA());
        System.out.println("mininum B = "+sm.getMinB());
        System.out.println("reconstructed mini = " + sm.getMinA().add(sm.getMinB()).mod(twoToL));


    }

    private void printArr(BigInteger[] arr){
        for (BigInteger item: arr){
            System.out.print(item + " ");
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
