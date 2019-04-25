package kNNQuery;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Cloud<T> {
    private ArrayList<T> hospitalList = new ArrayList<>();
    private ArrayList<Triple<T, T, T>>  kNNList = new ArrayList<>();
    private Set<Integer> winnerSet = new HashSet<>();


    public Cloud(){

    }

    public void addHospital(T hospital){
        hospitalList.add(hospital);
    }

    public int getHospitalSize(){
        return hospitalList.size();
    }

    public ArrayList<Triple<T, Integer, Integer> >getFinalResult(){
        //todo
        return null;
    }
    public Hospital<T> getHospital(int index){
        return (Hospital<T>)hospitalList.get(index);
    }
    public void setWinnerSet(int hospitalIndex){
        this.winnerSet.add(hospitalIndex);
    }

    public Set<Integer> getWinnerSet(){
        return winnerSet;
    }

    public int setSize(){
        return winnerSet.size();
    }



}
