package secureExactEditDistance;

import helper.Helper;
import secureBranch.SecureBranch;
import secureMinimumSelection.SecureMiniSelection;

import java.math.BigInteger;

/**
 * Secure Exact Edit Distance protocol
 */
public class SecureExactEditDistance{
    private int bitSize;
    private BigInteger dEDA;
    private BigInteger dEDB;
    private BigInteger twoToL;

    public SecureExactEditDistance(int bitSize){
        this.bitSize = bitSize;
        this.twoToL = (BigInteger.TWO).pow(bitSize);
    }

    /**
     * Compute edit distance between x and y sequences by using Wagner-Fisher Algorithm
     * The finial shared edit distance are set to field dEDA and dEDB
     * @param xA A's x shared sequence
     * @param xB B's x shared sequence
     * @param yA A's y shared sequence
     * @param yB B's y shared sequence
     * @throws Exception
     */
    public void setDistance(BigInteger[]xA, BigInteger[] xB, BigInteger[] yA, BigInteger[] yB) throws Exception{
        if (xA.length != xB.length || yA.length != yB.length){
            throw new IllegalArgumentException(); // shared sequence between two parties has to be match
        }

        int n1= xA.length;
        int n2 = yA.length;
        if( twoToL.compareTo(BigInteger.valueOf(getMax(n1, n2))) < 0){
            System.out.println("2^l should be larger than max(n1, n2)!");
            throw new IllegalArgumentException();
        }


        BigInteger[][] deltaA = new BigInteger[n1+1][n2+1];
        BigInteger[][] deltaB = new BigInteger[n1+1][n2+1];

        for(int i = 0; i<= n1; i++){
            deltaA[i][0] =BigInteger.valueOf(i);
            deltaB[i][0] = BigInteger.ZERO;
        }
        for(int j = 0; j<= n2; j++){
            deltaA[0][j] =BigInteger.valueOf(j);
            deltaB[0][j] = BigInteger.ZERO;
        }

        BigInteger cDelA = BigInteger.ONE;
        BigInteger cDelB = BigInteger.ZERO;

        BigInteger cInA = BigInteger.ONE;
        BigInteger cInB = BigInteger.ZERO;

        BigInteger z0A = BigInteger.ZERO;
        BigInteger z0B = BigInteger.ZERO;

        BigInteger z1A= BigInteger.ONE;
        BigInteger z1B = BigInteger.ZERO;


        SecureBranch sb1 = new SecureBranch(bitSize);
        SecureBranch sb2 = new SecureBranch(bitSize);
        SecureMiniSelection sms = new SecureMiniSelection(bitSize);


        for (int i = 1; i<= n1; i++){
            for(int j=1; j<=n2; j++){
                BigInteger t1A = yA[i-1].add(BigInteger.ONE).mod(twoToL);
                BigInteger t1B = yB[i-1];

                BigInteger t2A = yA[i-1].subtract(BigInteger.ONE).mod(twoToL);
                BigInteger t2B = yB[i-1];

                sb1.addAndCompare(sb1.genArray(t1A,xA[i-1]),sb1.genArray(t1B,xB[i-1]), sb1.genArray(z0A, z1A), sb1.genArray(z0B, z1B));
                BigInteger t3A = sb1.getYA();
                BigInteger t3B = sb1.getYB();

                sb2.addAndCompare(sb2.genArray(xA[i-1],t2A),sb2.genArray(xB[i-1],t2B), sb2.genArray(t3A, z1A), sb2.genArray(t3B, z1B));
                BigInteger cSubA = sb2.getYA();
                BigInteger cSubB = sb2.getYB();

                /*Helper<BigInteger> helper = new Helper<>(bitSize);
                BigInteger reconstructedX = helper.reconstruct(xA[i-1], xB[i-1], bitSize);
                BigInteger reconstructedY = helper.reconstruct(yA[i-1],yB[i-1],bitSize);
                BigInteger cSubA;
                BigInteger cSubB;
                if(reconstructedX.compareTo(reconstructedY)==0){
                    cSubA = BigInteger.ZERO;
                    cSubB = BigInteger.ZERO;
                }
                else{
                    cSubA = BigInteger.ZERO;
                    cSubB  = BigInteger.ONE;
                }*/

                BigInteger term1A =  deltaA[i-1][j].add(cDelA).mod(twoToL);
                BigInteger term1B =  deltaB[i-1][j].add(cDelB).mod(twoToL);

                BigInteger term2A =  deltaA[i][j-1].add(cInA).mod(twoToL);
                BigInteger term2B =  deltaB[i][j-1].add(cInB).mod(twoToL);

                BigInteger term3A =  deltaA[i-1][j-1].add(cSubA).mod(twoToL);
                BigInteger term3B =  deltaB[i-1][j-1].add(cSubB).mod(twoToL);


                sms.getMini(sms.genArray(term1A, term2A, term3A), sms.genArray(term1B, term2B, term3B));

                deltaA[i][j] = sms.getMinA();
                deltaB[i][j] = sms.getMinB();

            }
        }
        //check boundary array index;
        dEDA = deltaA[n1][n2];
        dEDB = deltaB[n2][n2];
    }

    /**
     * Getter for dEDA
     * @return
     */
    public BigInteger getDedA(){
        return dEDA;
    }

    /**
     * Getter for dEDB
     * @return
     */
    public BigInteger getDedB(){
        return dEDB;
    }

    private int getMax(int n1, int n2){
        if(n1>= n2){
            return n1;
        }
        else{
            return n2;
        }
    }

}
