import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class GCTest{
    public static void main(String[] args) throws IOException {
        String circuitFile = "cmp.cir";
        String serverInputFile = "b_side";
        String clientInputFile = "a_side";
        String cmd = "testfiles";
        garbledCircuitTest(cmd, circuitFile,serverInputFile,clientInputFile);
    }

    private static void garbledCircuitTest(String cmd, String circuitFile, String serverInputFile, String clientInputFile) throws IOException {
        try{
           // ProcessBuilder gcProcess = new ProcessBuilder();
            //gcProcess.command(cmd,circuitFile);
            ProcessBuilder gcProcess = new ProcessBuilder("/home/yi/Workspace/CFQ/GCParser/runtestgcparser", circuitFile,serverInputFile,clientInputFile );
            gcProcess.directory(new File("/home/yi/Workspace/CFQ/GCParser"));
            //Map<String,String> env = gcProcess.environment();
            //env.put("PATH", "/home/yi/Workspace/CFQ/GCParser");
           // gcProcess.redirectErrorStream(true);
            Process p = gcProcess.start();

            p.waitFor();
            BufferedReader reader=new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
         catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        //ProcessBuilder pdTest = new ProcessBuilder("cal");
        //Process pt = pdTest.start();

        /*BufferedReader in = new BufferedReader(new InputStreamReader(gcProcess.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        gcProcess.waitFor();
        System.out.println("ok!");

        in.close();
        System.exit(0);*/

      //Process pro = Runtime.getRuntime().exec("/home/yi/Workspace/GCParser/runtestgcparser /home/yi/Workspace/GCParser/cmp.cir  /home/yi/Workspace/GCParser/b_side /home/yi/Workspace/GCParser/a_side");
    }
}
