import org.junit.jupiter.api.Test;
import secureShuffle.OfflineShuffling;
import secureShuffle.OfflineShufflingPool;

import java.math.BigInteger;
import java.util.Arrays;

public class OfflineShufflingPoolTest {
    @Test
    void testOfflineShufflingPool(){
        int bitSize = 10;
        OfflineShuffling offlineShuffling = new OfflineShuffling();
        OfflineShufflingPool offlineShufflingPool = new OfflineShufflingPool(bitSize, offlineShuffling);

        Integer[] pi10 = offlineShufflingPool.getPi(10);
        BigInteger[] L210 = offlineShufflingPool.getL2(10);
        System.out.println("Pi:");
        System.out.println(Arrays.toString(pi10));
        System.out.println("L2");
        System.out.println(Arrays.toString(L210));

        Integer[] pi5 = offlineShufflingPool.getPi(5);
        BigInteger[] L25 = offlineShufflingPool.getL2(5);
        System.out.println("Pi:");
        System.out.println(Arrays.toString(pi5));
        System.out.println("L2");
        System.out.println(Arrays.toString(L25));

        Integer[] pi2 = offlineShufflingPool.getPi(20);
        BigInteger[] L22 = offlineShufflingPool.getL2(20);
        System.out.println("Pi:");
        System.out.println(Arrays.toString(pi2));
        System.out.println("L2");
        System.out.println(Arrays.toString(L22));


    }
}
