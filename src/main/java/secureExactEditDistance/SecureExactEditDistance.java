package secureExactEditDistance;

import secureBranch.SecureBranch;
import secureMinimumSelection.SecureMiniSelection;

import java.math.BigInteger;

public class SecureExactEditDistance{
    private int bitSize = 10;
    private BigInteger dEDA;
    private BigInteger dEDB;
    private BigInteger twoToL= (BigInteger.TWO).pow(bitSize);

    public SecureExactEditDistance(int bitSize){
        this.bitSize = bitSize;
        this.twoToL = (BigInteger.TWO).pow(bitSize);
    }

    public void setDistance(BigInteger[]xA, BigInteger[] xB, BigInteger[] yA, BigInteger[] yB) throws Exception{
        if (xA.length != xB.length || yA.length != yB.length){
            throw new IllegalArgumentException();
        }
        int n1= xA.length;
        int n2 = yA.length;

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


        for (int j = 1; j<= n2; j++){
            for(int i=1; i<=n1; i++){
                BigInteger t1A = yA[i-1].add(BigInteger.ONE).mod(twoToL);
                BigInteger t2A = yA[i-1].subtract(BigInteger.ONE).mod(twoToL);
                BigInteger t1B = yB[i-1];
                BigInteger t2B = yB[i-1];

                sb1.addAndCompare(sb1.genArray(t1A,t1B),sb1.genArray(xA[i-1],xB[i-1]), sb1.genArray(z0A, z1A), sb1.genArray(z0B, z1B));
                BigInteger t3A = sb1.getYA();
                BigInteger t3B = sb1.getYB();

                sb2.addAndCompare(sb2.genArray(xA[i-1],xB[i-1]),sb2.genArray(t2A,t2B), sb2.genArray(t3A, t3B), sb2.genArray(z1A, z1B));
                BigInteger cSubA = sb2.getYA();
                BigInteger cSubB = sb2.getYB();

                sms.getMini(sms.genArray(deltaA[i-1][j].add(cDelA).mod(twoToL), deltaA[i][j-1].add(cInA).mod(twoToL), deltaA[i-1][j-1].add(cSubA).mod(twoToL)),
                        sms.genArray(deltaB[i-1][j].add(cDelB).mod(twoToL), deltaB[i][j-1].add(cInB).mod(twoToL), deltaB[i-1][j-1].add(cSubB).mod(twoToL)));
                deltaA[i][j] = sms.getMinA();
                deltaB[i][j] = sms.getMinB();

            }
        }
        //check boundary array index;
        dEDA = deltaA[n1][n2];
        dEDB = deltaB[n2][n2];
    }

    public BigInteger getDedA(){
        return dEDA;
    }

    public BigInteger getDedB(){
        return dEDB;
    }

}
