
import io.CreateInputFile;
import io.GCOutAccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class GCTest{
    public static void main(String[] args) throws Exception {
        String circuitFile = "cmp.cir";
        String serverInputFile = "b_side";
        String clientInputFile = "a_side";
        String cmd = "/home/yi/Workspace/CFQ/GCParser/runtestgcparser";
        String gcDir = "/home/yi/Workspace/CFQ/GCParser";
        String clientFileName = "GCParser/results/siclientout";
        String serverFileName = "GCParser/results/siserverout";

        garbledCircuitTest(cmd, gcDir,circuitFile,serverInputFile,clientInputFile, clientFileName, serverFileName);
    }

    private static void garbledCircuitTest(String cmd, String gcDir, String circuitFile, String serverInputFile, String clientInputFile, String clientFileName, String serverFileName) throws Exception {
        try{

            CreateInputFile ciA = new CreateInputFile("a_side");
            CreateInputFile ciB = new CreateInputFile("b_side");

            ciA.setClientVar(BigInteger.valueOf(2220));
            ciB.setSeverVar(BigInteger.valueOf(1248));


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
            System.out.println(gcClientOut.readResult());
            System.out.println(gcSeverOut.readResult());

        }
         catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
