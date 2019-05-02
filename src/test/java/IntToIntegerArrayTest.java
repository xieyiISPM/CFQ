import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Arrays;

public class IntToIntegerArrayTest {

    @Test
    void testIntToIntegerArray(){
        SecureRandom srand = new SecureRandom();
        int n = 10;
        int[] arr = new int[n];
        for(int i = 0;i< n; i++){
            arr[i] = srand.nextInt(100);
        }
        System.out.println(Arrays.toString(arr));

        Integer[] newArr =  Arrays.stream(arr).boxed().toArray( Integer[]::new );
        System.out.println(Arrays.toString(newArr));


    }
}
