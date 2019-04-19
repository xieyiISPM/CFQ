import helper.Helper;
import kNNQuery.Hospital;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class HospitalCloudTest {
    @Test
    void testHospital(){
        Hospital<BigInteger>[] hospital =  new Hospital[]{ new Hospital(),new Hospital(),new Hospital(),new Hospital()} ;

        int bitSize = 10;
        int arraySize =8;
        int records = 5;
        int k = 3;

        Helper queryHelper= new Helper(bitSize);
        queryHelper.genQuery(arraySize);BigInteger[] QA = queryHelper.getQueryA();
        BigInteger[] QB = queryHelper.getQueryB();
        BigInteger[] Q = queryHelper.getOriginalQuery();

        Helper[] helper = new Helper[]{new Helper(bitSize), new Helper(bitSize),new Helper(bitSize), new Helper(bitSize)};
        for(int i=0; i< hospital.length; i++){
            hospital[i].addQuery(QA);
            helper[i].genGS(arraySize,records);
            BigInteger[][] gsA = helper[i].getGSA();
            hospital[i].addAllSequenceData(gsA);
        }
        System.out.println("Add records successful!");
    }
}
