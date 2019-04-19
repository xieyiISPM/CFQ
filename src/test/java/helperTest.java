import helper.Helper;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class helperTest {
    @Test
    void testGenQuery(){
        int bitSize = 5;
        int arraySize = 10;
        Helper helper = new Helper(bitSize);
        helper.genQuery(arraySize);
        BigInteger[] QA = helper.getQueryA();
        BigInteger[] QB = helper.getQueryB();
        BigInteger[] Q = helper.getOriginalQuery();
        System.out.println("Q:");
        helper.printArr(Q);
        System.out.println("QA:");
        helper.printArr(QA);
        System.out.println("QB:");
        helper.printArr(QB);
    }

    @Test
    void testGenGS(){
        int bitSize = 12;
        int arraySize = 15;
        int records = 3;
        Helper helper = new Helper(bitSize);
        helper.genGS(arraySize, records);
        BigInteger[][] GSA = helper.getGSA();
        BigInteger[][] GSB = helper.getGSB();
        BigInteger[][] GS = helper.getOriginalGS();
        System.out.println("GS:");
        for(int i= 0; i< records;i++){
            System.out.print("record-"+ i + ": ");
            helper.printArr(GS[i]);

        }

        System.out.println("GSA:");
        for(int i= 0; i< records;i++){
            System.out.print("record-"+ i + ": ");
            helper.printArr(GSA[i]);
        }

        System.out.println("GSB:");
        for(int i= 0; i< records;i++){
            System.out.print("record-"+ i + ": ");
            helper.printArr(GSB[i]);
        }
    }
}
