package kNNQuery;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;

public class Cloud<E,T> {
    private ArrayList<E> hospitalList = new ArrayList<>();
    private ArrayList<Triple<T, T, T>>  kNNList = new ArrayList<>();


    public Cloud(){

    }

    public void addHospital(E hospital){
        hospitalList.add(hospital);
    }

    public int getHospitalsize(){
        return hospitalList.size();
    }

    public Pair<T, T> getHospitalIDIndexInfo(int index){
        Triple<T, T, T> hospitalIDIndexDistInfo = kNNList.get(index);
        Pair<T,T> hospitalIDIndexInfo = new ImmutablePair<>(hospitalIDIndexDistInfo.getLeft(),hospitalIDIndexDistInfo.getMiddle());
        return hospitalIDIndexInfo;
    }



}
