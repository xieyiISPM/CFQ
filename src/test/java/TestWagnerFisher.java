import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

public class TestWagnerFisher {

    @Test
    void testWagnerFisher(){

        String strX = "abreitsjour";
        String strY = "arbeitsjournal";
        char[] charX = strX.toCharArray();
        char[] charY = strY.toCharArray();
        BigInteger[] x  = new BigInteger[charX.length];
        BigInteger[] y = new BigInteger[charY.length];
        int n1 = charX.length ;
        int n2 = charY.length;


        for (int i = 0; i< n1; i++){
            x[i] = BigInteger.valueOf(Character.getNumericValue(charX[i]));
        }
        for (int i = 0; i< n2; i++){
            y[i] = BigInteger.valueOf(Character.getNumericValue(charY[i]));
        }


        BigInteger[][] distance = new BigInteger[n1 + 1][n2 +1];

        for(int i =0; i<=n1; i++){
            distance[i][0]= BigInteger.valueOf(i);
        }
        for(int j = 0; j<=n2; j++ ){
            distance[0][j] = BigInteger.valueOf(j);
        }
        BigInteger cDel = BigInteger.ONE;
        BigInteger cIns = BigInteger.ONE;
        BigInteger cSub;
        for (int i = 1; i<=n1; i++ ){
            for(int j= 1; j <=n2; j++){
                if(x[i-1].compareTo(y[j-1])==0){
                    cSub = BigInteger.ZERO;
                }
                else {
                    cSub = BigInteger.ONE;
                }
                distance[i][j] = min(distance[i-1][j].add(cDel), distance[i][j-1].add(cIns),distance[i-1][j-1].add(cSub));
            }
        }

        for(int i = 0; i<= n1;i++){
            for(int j= 0; j<= n2; j++){
                System.out.print(distance[i][j] + " ");
            }
            System.out.println();
        }
        System.out.print("distance = ");
        System.out.println(distance[n1][n2]);
    }

    private BigInteger min(BigInteger big1, BigInteger big2, BigInteger big3){
        if(big1.compareTo(big2) < 0){
            if(big1.compareTo(big3) < 0){
                return big1;
            }
            else{
                return big3;
            }
        }
        else{
            if(big3.compareTo(big2)<0){
                return big3;
            }
            else{
                return big2;
            }
        }
    }

    @Test
    void testMin(){
        SecureRandom srd = new SecureRandom();
        int testRound = 30;
        int bitSize = 5;
        for (int i = 0; i < testRound; i++){
            BigInteger big1 = new BigInteger(bitSize, srd);
            BigInteger big2 = new BigInteger(bitSize, srd);
            BigInteger big3 = new BigInteger(bitSize, srd);
            System.out.println("big1 = " + big1 + " big2 = " + big2 + " big3 = " + big3);
            System.out.println("minimum number = " + min(big1, big2 , big3));

        }
    }
}
