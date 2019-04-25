package kNNQuery;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import secureCompare.SecureCompare;

import java.math.BigInteger;
import java.util.ArrayList;

public class KNNQuery<T> {
    int bitSize;
    ArrayList<Triple<T,Integer, Integer>> finalHKNN;
    ArrayList<Triple<T,Integer, Integer>> finalCKNN;

    public KNNQuery(int bitSize) {
        this.bitSize = bitSize;
    }

    public void genKNN(Cloud<T> cloud, Hospital<T>[] hospitals, int k) {
        int hospitalNum = hospitals.length;
        for (int i = 0; i < k; i++) {

            int next = 1;
            int winnerID = 0;
            while (next < hospitalNum) {
                BigInteger distHA = (BigInteger) ((Pair<BigInteger, BigInteger>) hospitals[winnerID].peekIndexDistancePair()).getRight();
                BigInteger distHB = (BigInteger) ((Pair<BigInteger, BigInteger>) hospitals[next].peekIndexDistancePair()).getRight();
                BigInteger distCA = (BigInteger) ((Pair<BigInteger, BigInteger>) cloud.getHospital(winnerID).peekIndexDistancePair()).getRight();
                BigInteger distCB = (BigInteger) ((Pair<BigInteger, BigInteger>) cloud.getHospital(next).peekIndexDistancePair()).getRight();

                SecureCompare sc = new SecureCompare(bitSize);
                int compareResult = sc.secureCompare(distHA, distCA, distHB, distCB);
                if (compareResult > 0) {
                    winnerID = next;
                }
                next++;

            }
            hospitals[winnerID].popIndexDistancePair();
            cloud.getHospital(winnerID).popIndexDistancePair();
            System.out.println("winner ID =" + winnerID);
            int position = hospitals[winnerID].getIndexDistancePairListSize() - hospitals[winnerID].getIndexDistancePairStackSize() - 1;

            hospitals[winnerID].setWinInfoList(position, i, true);
            cloud.setWinnerSet(winnerID);
        }

        this.finalHKNN = new ArrayList<>();
        this.finalCKNN = new ArrayList<>();

        for (int hospitalID : cloud.getWinnerSet()) {
            hospitals[hospitalID].genFinalResults();
            cloud.getHospital(hospitalID).genFinalResults();
            finalHKNN.addAll(hospitals[hospitalID].getFinalResults());
            finalCKNN.addAll(cloud.getHospital(hospitalID).getFinalResults());
        }
    }

    public ArrayList<Triple<T, Integer, Integer>> getFinalHKNN(){
        return finalHKNN;
    }

    public ArrayList<Triple<T, Integer, Integer>> getFinalCKNN(){
        return finalCKNN;
    }
}
