package secureShuffle;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class OfflineShufflingPool {
    //Integer is to store offlineshuffling array size
    private Map<Integer, Pair<Integer[],BigInteger[] > > offlineShufflingSequence = new HashMap<>();
    private SSF ssf;
    private OfflineShuffling offlineShuffling;

    public OfflineShufflingPool(int bitSize, OfflineShuffling offlineShuffling){
        this.ssf = new SSF(bitSize);
        this.offlineShuffling  = offlineShuffling;
    }

    public void addToPool(int arraySize){
        InitSet initSet = new InitSet();
        Integer[] pi = initSet.genPi(arraySize);
        BigInteger[] L2 =  ssf.getOfflineOutput(arraySize, offlineShuffling, pi);
        Pair<Integer[],BigInteger[] >  piL2Pair = new ImmutablePair<>(pi, L2);
        if(!offlineShufflingSequence.containsKey(arraySize)){
            offlineShufflingSequence.put(arraySize, piL2Pair);
        }
    }

    public Pair<Integer[], BigInteger[]> getPiAndL2(int arraySize){
        if(offlineShufflingSequence.containsKey(arraySize)){
            return offlineShufflingSequence.get(arraySize);
        }
        else{
            addToPool(arraySize);
            return offlineShufflingSequence.get(arraySize);
        }
    }

    public Integer[] getPi(int arraySize){
        if(offlineShufflingSequence.containsKey(arraySize)){
            return offlineShufflingSequence.get(arraySize).getLeft();

        }
        else{
            addToPool(arraySize);
            return offlineShufflingSequence.get(arraySize).getLeft();
        }
    }

    public BigInteger[] getL2(int arraySize){
        if(offlineShufflingSequence.containsKey(arraySize)){
            return offlineShufflingSequence.get(arraySize).getRight();
        }
        else{
            addToPool(arraySize);
            return offlineShufflingSequence.get(arraySize).getRight();

        }
    }

}


