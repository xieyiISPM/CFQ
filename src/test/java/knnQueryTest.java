import kNNQuery.Hospital;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class knnQueryTest {
    @Test
    void testHospital(){
        Hospital<BigInteger> hospital1 = new Hospital<>(0);
        Hospital<BigInteger> hospital2 = new Hospital<>(1);
        Hospital<BigInteger> hospital3 = new Hospital<>(2);
        Hospital<BigInteger> hospital4 = new Hospital<>(3);



        System.out.println("hospital1! object: " + hospital1);
        System.out.println("hospital1! object: " + hospital2);
        System.out.println("hospital1! object: " + hospital3);
        System.out.println("hospital1! object: " + hospital4);


    }


}
