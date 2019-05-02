package kNNQuery;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Cloud<T> {
    private ArrayList<T> hospitalList = new ArrayList<>();

    //Winner set save winner hospital's IDs
    private Set<Integer> winnerSet = new HashSet<>();


    public Cloud(){

    }

    public void addHospital(T hospital){
        hospitalList.add(hospital);
    }

    public int getHospitalSize(){
        return hospitalList.size();
    }

    public Hospital<T> getHospital(int index){
        return (Hospital<T>)hospitalList.get(index);
    }

    public void setWinnerSet(int hospitalID){
        this.winnerSet.add(hospitalID);
    }

    public Set<Integer> getWinnerSet(){
        return winnerSet;
    }

    public int getWinnerSetSize(){
        return winnerSet.size();
    }

}
