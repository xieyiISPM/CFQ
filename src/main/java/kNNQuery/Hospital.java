package kNNQuery;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Class to save Hospital Information
 * @param <T>
 */
public class Hospital<T> {
    private T[][] GS; //genomic sequences
    private T[] query; //query shared with hospital

    private Pair<T, T>[] indexDistancePairList ;
    private Stack<Pair<T,T>> indexDistancePairStack = new Stack<>(); //Use stack to help final sort
    //private BigInteger winInfo;
    private ArrayList<Triple<Integer, Integer, Boolean>> indexOrderIsWinList = new ArrayList<>();
    private ArrayList<Triple<T, Integer, Integer>> finalResultsList = new ArrayList<>(); //index of database, hospitalID, finalOrder)
    private int hospitalID;

    public Hospital(int hospitalID) {
        this.hospitalID = hospitalID;
    }

    public void addQuery(T[] query){
        this.query=query;
    }

    public void addAllSequenceData(T[][] S) {
            this.GS=S;
    }


    public T[] getSequenceData(int index) {
        return GS[index];
    }

    public void addTopKIndexDistancePair(Pair<T, T>[] indexDistancePairArray) {
            this.indexDistancePairList=indexDistancePairArray;
            for(int i= indexDistancePairArray.length-1; i>= 0; i--){
                indexDistancePairStack.push(indexDistancePairArray[i]);
            }
    }

    public Pair<T, T> getIndexDistancePair(int index) {
        return this.indexDistancePairList[index];
    }

    public Pair<T,T> popIndexDistancePair() {
        return indexDistancePairStack.pop();
    }

    public Pair<T,T> peekIndexDistancePair() {
        return indexDistancePairStack.peek();
    }

    public int getGSSize() {
        return GS.length;
    }

    public int getIndexDistancePairListSize(){
        return indexDistancePairList.length;
    }

    public int getIndexDistancePairStackSize(){
        return indexDistancePairStack.size();
    }


    public void genFinalResults() {
        if(indexOrderIsWinList != null){
            for(Triple<Integer, Integer, Boolean> element: indexOrderIsWinList){
                if(element.getRight()==true){
                    finalResultsList.add(new ImmutableTriple<>(indexDistancePairList[element.getLeft()].getLeft(), hospitalID, element.getLeft()));
                }
            }
        }
    }

    public ArrayList<Triple<T, Integer, Integer>> getFinalResults(){
        return finalResultsList;
    }

    public void setWinInfoList(Integer index, Integer order, Boolean  isWin) {
        Triple<Integer, Integer, Boolean> winInfoTriple = new ImmutableTriple<>(index, order, isWin);
        indexOrderIsWinList.add(winInfoTriple);
    }

    public T[][] getGS(){
        return GS;
    }
    public T[] getQuery(){
        return query;
    }

    public Pair[] getAllIndexDistancePair(){
        return indexDistancePairList;
    }

    public Pair[] getAllIndexDistancePairFromStack(){
        int size = indexDistancePairStack.size();
        Pair[] temp = new Pair[size];
        for(int i = size-1; i>= 0; i--){
            temp[i] = indexDistancePairStack.pop();
        }
        return temp;
    }

}
