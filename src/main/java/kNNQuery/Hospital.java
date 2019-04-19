package kNNQuery;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.util.ArrayList;

public class Hospital<T> {
    private ArrayList<T[]> GS = new ArrayList<>();
    private T[] query;

    private ArrayList<Pair<T, T>> indexDistancePairList = new ArrayList<>() ;
    private BigInteger winInfo;

    public Hospital() {

    }

    public void addQuery(T[] query){
        this.query=query;
    }

    public void addAllSequenceData(T[][] S) {
        for (int i = 0; i < S.length; i++) {
            this.GS.add(S[i]);
        }
    }

    public void addSequenceDate(T[] data) {
        this.GS.add(data);
    }

    public T[] getSequenceData(int index) {
        return GS.get(index);
    }

    public void addAllIndexDistancePair(Pair<T, T>[] indexDistancePairArray) {
        for (Pair<T, T> element : indexDistancePairArray) {
            this.indexDistancePairList.add(element);
        }
    }

    public Pair<T, T> getIndexDistancePair(int index) {
        return this.indexDistancePairList.get(index);
    }

    public void removeFirstIndexDistancePair() {
        indexDistancePairList.remove(0);
    }

    public int getGSSize() {
        return GS.size();
    }

    public int getIndexDistancePairListSize(){
        return indexDistancePairList.size();
    }

    public BigInteger getWinInfo() {
        return this.winInfo;
    }

    public void setIsWinInfo(BigInteger winInfo) {
        this.winInfo = winInfo;
    }

}
