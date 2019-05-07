package secureShuffle;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * A offlineShuffling pool which has to be pre created to save computation time
 */
public class OfflineShufflingPool {
    //Integer is to store offlineshuffling array size
    //Pair is to store (pi[], L2)
    private Map<Integer, Pair<Integer[],BigInteger[] > > offlineShufflingSequence = new HashMap<>();
    private SSF ssf;
    private Map<Integer, OfflineShuffling> offlineShufflingMap = new HashMap<>();

    public OfflineShufflingPool(int bitSize){
        this.ssf = new SSF(bitSize);
    }

    public void addOfflineShufflingToPool(int arraySize, OfflineShuffling offlineShuffling, Integer[] pi){
        offlineShuffling.genVArray(arraySize);
        offlineShuffling.genUArray(arraySize);
        offlineShuffling.addPi(pi);
        this.offlineShufflingMap.put(arraySize, offlineShuffling);


    }

    public void addToPool(int arraySize){
        /*InitSet initSet = new InitSet();
        Integer[] pi = initSet.genPi(arraySize);*/
        Integer[] pi = offlineShufflingMap.get(arraySize).getPi();
        BigInteger[] L2 =  ssf.getOfflineOutput(arraySize, offlineShufflingMap.get(arraySize), pi;
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


