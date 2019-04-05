package secureBranch;

import gc.GarbledCircuit;
import io.CreateADDCMPInputFile;
import io.GCOutAccess;
import secureShuffle.OfflineShuffling;
import secureShuffle.SSF;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class SecureBranch {
    private int bitSize = 10;
    private BigInteger yOutputA = null;
    private BigInteger yOutputB = null;

    private String circuitFile = "ADD-CMP.cir";
    private String serverInputFile = "b-input";
    private String clientInputFile = "a-input";
    private String cmd = "/home/yi/Workspace/CFQ/GCParser/runtestgcparser";
    private String gcDir = "/home/yi/Workspace/CFQ/GCParser";
    private String clientFileName = "GCParser/results/siclientout";
    private String serverFileName = "GCParser/results/siserverout";


    public SecureBranch(int bitSize){
        this.bitSize = bitSize;
    }

    public void addAndCompare(BigInteger[] xA, BigInteger[] xB, BigInteger[] yA, BigInteger[] yB) throws Exception{
        if(xA.length != xB.length || yA.length!= yB.length || xA.length != yA.length || xA.length != 2){
            throw new IllegalArgumentException("Array sizes do not match!");
        }

        int arraySizeX = xA.length;

        SSF ssf = new SSF(bitSize);
        int[] pi = ssf.getPi(arraySizeX);
        OfflineShuffling offlineShufflingX = new OfflineShuffling();
        BigInteger[] xHPrime = ssf.getOfflineOutput(arraySizeX, offlineShufflingX);
        BigInteger[] xCPrime = ssf.getOnlineOuptut(arraySizeX,xA, xB,offlineShufflingX, pi );

        OfflineShuffling offlineShufflingY = new OfflineShuffling();
        BigInteger[] yHPrime = ssf.getOfflineOutput(arraySizeX, offlineShufflingY);
        BigInteger[] yCPrime = ssf.getOnlineOuptut(arraySizeX,xA, xB,offlineShufflingY,pi );

        CreateADDCMPInputFile ciA = new CreateADDCMPInputFile("a-input");
        CreateADDCMPInputFile ciB = new CreateADDCMPInputFile("b-input");

        ciA.setClientVar(xHPrime[0], xCPrime[0]);
        ciB.setSeverVar(xHPrime[1], xCPrime[1]);

        //must have absolute path here!!!
        ProcessBuilder gcProcess = new ProcessBuilder(cmd, circuitFile,serverInputFile,clientInputFile );
        gcProcess.directory(new File(gcDir));

        Process p = gcProcess.start();

        p.waitFor();
        BufferedReader reader=new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        String line;
        while((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        GCOutAccess gcClientOut = new GCOutAccess(clientFileName);
        GCOutAccess gcSeverOut = new GCOutAccess(serverFileName);

        int thetaClient = gcClientOut.readResult();
        int thetaSever = gcSeverOut.readResult();

        //check which should be assigned to yOutputA/yOutputB
        if(thetaClient==thetaSever &&  thetaClient== 1){
            yOutputA = yHPrime[0];
            yOutputB = yCPrime[0];

        }
        else{
            yOutputA = yHPrime[1];
            yOutputB = yCPrime[1];
        }

    }

    public BigInteger[] genArray(BigInteger bigInt1, BigInteger bigInt2){
        BigInteger[] arr= new BigInteger[2];
        arr[0] = bigInt1;
        arr[1] = bigInt2;
        return arr;

    }

    public BigInteger getYA(){
        return yOutputA;
    }

    public BigInteger getYB(){
        return yOutputB;
    }




}
