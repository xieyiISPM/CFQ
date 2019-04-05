package secureExactEditDistance;

import secureBranch.SecureBranch;
import secureMinimumSelection.SecureMiniSelection;

import java.math.BigInteger;

public class SecureExactEditDistance{
    private int bitSize = 10;
    private BigInteger dEDA;
    private BigInteger dEDB;

    public SecureExactEditDistance(int bitSize){
        this.bitSize = bitSize;
    }

    public void setDistance(BigInteger[]xA, BigInteger[] xB, BigInteger[] yA, BigInteger[] yB) throws Exception{
        if (xA.length != xB.length || yA.length != yB.length){
            throw new IllegalArgumentException();
        }
        int n1= xA.length;
        int n2 = yA.length;

        BigInteger[][] deltaA = new BigInteger[n1-1][n2-1];
        BigInteger[][] deltaB = new BigInteger[n1-1][n2-1];

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

        for (int i = 1; i< n1; i++){
            for(int j=1; j<n2; j++){
                BigInteger t1A = yA[i].add(BigInteger.ONE);
                BigInteger t2A = yA[i].subtract(BigInteger.ONE);
                BigInteger t1B = yB[i];
                BigInteger t2B = yB[i];

                SecureBranch sb1 = new SecureBranch(bitSize);
                String serverOutputFile1 = null; //todo
                String clientOutputFile1 = null;//todo
                sb1.addAndCompare(sb1.genArray(t1A,xA[i]),sb1.genArray(t1B,xB[i]), sb1.genArray(z0A, z1A), sb1.genArray(z0B, z1B));
                BigInteger t3A = sb1.getYA();
                BigInteger t3B = sb1.getYB();

                SecureBranch sb2 = new SecureBranch(bitSize);
                String serverOutputFile2 = null; //todo
                String clientOutputFile2 = null; //todo
                sb1.addAndCompare(sb1.genArray(xA[i],t2A),sb1.genArray(xB[i],t2B), sb1.genArray(t3A, z1A), sb1.genArray(t3B, z1B));
                BigInteger cSubA = sb2.getYA();
                BigInteger cSubB = sb2.getYB();

                String serverOutputFile3 = null;//todo
                String clientOutputFile3  = null;//todo

                SecureMiniSelection sms = new SecureMiniSelection(bitSize);
                sms.getMini(sms.genArray(deltaA[i-1][j].add(cDelA), deltaA[i][j-1].add(cInA), deltaA[i-1][j-1].add(cSubA)),
                        sms.genArray(deltaB[i-1][j].add(cDelB), deltaB[i][j-1].add(cInB), deltaB[i-1][j-1].add(cSubB)),
                        serverOutputFile3,clientOutputFile3);
                deltaA[i][j] = sms.getMinA();
                deltaB[i][j] = sms.getMinB();

            }
        }
        //check boundary array index;
        dEDA = deltaA[n1-1][n2-1];
        dEDB = deltaB[n2-1][n2-1];
    }

    public BigInteger getEDA(){
        return dEDA;
    }

    public BigInteger getEDB(){
        return dEDB;
    }


}
