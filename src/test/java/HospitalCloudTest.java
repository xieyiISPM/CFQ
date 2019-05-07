import helper.Helper;
import kNNQuery.Cloud;
import kNNQuery.Hospital;
import kNNQuery.KNNQuery;
import org.junit.jupiter.api.Test;
import secureShuffle.OfflineShuffling;
import secureShuffle.OfflineShufflingPool;
import topkQuery.TopKQuery;

import java.math.BigInteger;

public class HospitalCloudTest {
    @Test
    void testHospital(){
        Hospital<BigInteger>[] hospital =  new Hospital[]{ new Hospital(0),new Hospital(1),new Hospital(2),new Hospital(3)} ;

        int bitSize = 10;
        int arraySize =10;
        int records = 15;
        int k = 3;

        Helper queryHelper= new Helper(bitSize);
        queryHelper.genQuery(arraySize);
        BigInteger[] QA = queryHelper.getQueryA();
        BigInteger[] QB = queryHelper.getQueryB();
        BigInteger[] Q = queryHelper.getOriginalQuery();

        Helper[] helperArr = new Helper[]{new Helper(bitSize), new Helper(bitSize),new Helper(bitSize), new Helper(bitSize)};
        for(int i=0; i< hospital.length; i++){
            hospital[i].addQuery(QA);
            helperArr[i].genGS(arraySize,records);
            BigInteger[][] gsA = helperArr[i].getGSA();
            hospital[i].addAllSequenceData(gsA);
        }
        int i = 0;
        for(Helper h: helperArr){
            System.out.println("Hospital-"+i + "-SA  " + hospital[i].getGSSize()+ " records:");
            h.GSRecordsPrinter(hospital[i].getGS());
            System.out.println();
            i++;
        }
        System.out.println("Add and retrieve records successful!");
    }

    @Test
    void testCloud(){
        int bitSize = 10;
        int arraySize =10;
        int records = 6;
        int k = 3;

        Cloud cloud = new Cloud();
        Hospital<BigInteger>[] hospital =  new Hospital[]{ new Hospital(0),new Hospital(1),new Hospital(2),new Hospital(3)} ;
        for(Hospital h: hospital){
            cloud.addHospital(h);
        }
        System.out.println(" Hospital size inside cloud: " + cloud.getHospitalSize());


        Helper queryHelper= new Helper(bitSize);
        queryHelper.genQuery(arraySize);
        BigInteger[] QB = queryHelper.getQueryA();


        Helper[] helperArr = new Helper[]{new Helper(bitSize), new Helper(bitSize),new Helper(bitSize), new Helper(bitSize)};
        for(int i=0; i< cloud.getHospitalSize(); i++){
            cloud.getHospital(i).addQuery(QB);
            helperArr[i].genGS(arraySize,records);
            BigInteger[][] gsB = helperArr[i].getGSB();
            cloud.getHospital(i).addAllSequenceData(gsB);
        }

        int i = 0;
        for(Helper h: helperArr){
            System.out.println("Cloud side: Hospital-"+i + "-SB  " + cloud.getHospital(i).getGSSize()+ " records:");
            h.GSRecordsPrinter(cloud.getHospital(i).getGS());
            System.out.println();
            i++;
        }
        System.out.println("Add and retrieve records successful!");
    }


    @Test
    void testHospitalCloudCombine() throws Exception{
        int bitSize = 10;
        int arraySize = 10;
        int records = 50;
        int k = 2;

        Cloud cloud = new Cloud();
        Hospital<BigInteger>[] hospital =  new Hospital[]{ new Hospital(0),new Hospital(1),new Hospital(2),new Hospital(3)} ;
        //Be careful! DO NOT ADD SOME HOSPITAL OBJECT TO BOTH SIDES
        Hospital<BigInteger>[] hospitalC =  new Hospital[]{ new Hospital(0),new Hospital(1),new Hospital(2),new Hospital(3)} ;

        for(Hospital h: hospitalC){
            cloud.addHospital(h);
        }
        System.out.println(" Hospital size inside cloud: " + cloud.getHospitalSize());

        OfflineShufflingPool pool = new OfflineShufflingPool(bitSize, new OfflineShuffling());

        Helper queryHelper= new Helper(bitSize);
        queryHelper.genQuery(arraySize);

        BigInteger[] QA = queryHelper.getQueryA();
        BigInteger[] QB = queryHelper.getQueryB();
        BigInteger[] Q = queryHelper.getOriginalQuery();


        Helper[] helperArr = new Helper[]{new Helper(bitSize), new Helper(bitSize),new Helper(bitSize), new Helper(bitSize)};
        for(int i=0; i< cloud.getHospitalSize(); i++){
            hospital[i].addQuery(QA);
            cloud.getHospital(i).addQuery(QB);
            helperArr[i].genGS(arraySize,records);
            BigInteger[][] gsB = helperArr[i].getGSB();
            BigInteger[][] gsA = helperArr[i].getGSA();
            cloud.getHospital(i).addAllSequenceData(gsB);
            hospital[i].addAllSequenceData(gsA);
        }

        int i = 0;
          for(Helper h: helperArr){
            System.out.println("Hospital-"+i + "-SA  " + hospital[i].getGSSize()+ " records:");
            h.GSRecordsPrinter(hospital[i].getGS());
            System.out.println("Query A: ");
            h.printArr(hospital[i].getQuery());
            System.out.println();

            System.out.println("Cloud side: Hospital-"+i + "-SB  " + cloud.getHospital(i).getGSSize()+ " records:");
            h.GSRecordsPrinter(cloud.getHospital(i).getGS());
            System.out.println("Query B: ");
            h.printArr((BigInteger[])cloud.getHospital(i).getQuery());
            System.out.println();

            System.out.println("GS Reconstruct: ");
            h.GSOriginalPrinter(h.getOriginalGS());
            System.out.println();
            System.out.println("Query Reconstruct:");
            h.printArr(Q);
            System.out.println();
            i++;
        }
        System.out.println("Add and retrieve records successful!");

        for(int j= 0; j< cloud.getHospitalSize(); j++){
            TopKQuery topKQuery = new TopKQuery(bitSize);
            topKQuery.secureQueryPreCompute(hospital[j].getQuery(), hospital[j].getGS(), (BigInteger[])cloud.getHospital(j).getQuery(), (BigInteger[][])cloud.getHospital(j).getGS(), pool);
            topKQuery.genTopKIndexDistTuple(k);
            hospital[j].addTopKIndexDistancePair(topKQuery.getTopKPairB());
            cloud.getHospital(j).addTopKIndexDistancePair(topKQuery.getTopKPairA());
        }

        /*for(int j=0; j< cloud.getHospitalSize(); j++){
            Helper<BigInteger> indexDistanceHelper = new Helper<>(bitSize);
            System.out.println("Hospital[" + j + "] top  " + k + " index and distance:");
            indexDistanceHelper.printAllIndexDistancePair(hospital[j].getAllIndexDistancePairFromStack());
        }
*/
        /*for(int j=0; j< cloud.getHospitalSize(); j++){
            Helper<BigInteger> indexDistanceHelper = new Helper<>(bitSize);
            System.out.println("Cloud-hospital[" + j + "] top " + k + " index and distance:");
            indexDistanceHelper.printAllIndexDistancePair(cloud.getHospital(j).getAllIndexDistancePairFromStack());
        }*/

        KNNQuery<BigInteger> knnFinal = new KNNQuery<>(bitSize);
        knnFinal.genKNN(cloud, hospital, k);






    }
}
