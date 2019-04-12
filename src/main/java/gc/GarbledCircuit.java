package gc;

import io.CreateADDCMPInputFile;
import io.GCOutAccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class GarbledCircuit {
    private String circuitFile = "ADD-CMP.cir";
    private String serverInputFile = "b-input";
    private String clientInputFile = "a-input";
    private String cmd = "/home/yi/Workspace/CFQ/GCParser/runtestgcparser";
    private String gcDir = "/home/yi/Workspace/CFQ/GCParser";
    private String clientFileName = "GCParser/results/siclientout";
    private String serverFileName = "GCParser/results/siserverout";

    public GarbledCircuit(String circuitFile, String serverInputFile, String clientInputFile, String clientFileName, String serverFileName){
        this.circuitFile = circuitFile;
        this.serverInputFile = serverInputFile;
        this.clientInputFile = clientInputFile;
        this.clientFileName = clientFileName;
        this.serverFileName = serverFileName;
    }

    public GarbledCircuit(){

    }

    public int GCADDCMPOutPut(BigInteger xA, BigInteger xB, BigInteger yA, BigInteger yB) throws Exception{
        CreateADDCMPInputFile ciA = new CreateADDCMPInputFile(clientInputFile);
        CreateADDCMPInputFile ciB = new CreateADDCMPInputFile(serverInputFile);

        ciA.setClientVar(xA, xB);
        ciB.setSeverVar(yA, yB);

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

        if(thetaClient != thetaSever){
            throw new IOException();
        }

        return thetaClient;

    }
}
