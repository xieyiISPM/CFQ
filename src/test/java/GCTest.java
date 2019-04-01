import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GCTest{
    public static void main(String[] args) throws IOException {
        String circuitFile = "/home/yi/Workspace/GCParser/cmp.cir";
        String serverInputFile = " /home/yi/Workspace/GCParser/b_side ";
        String clientInputFile = " /home/yi/Workspace/GCParser/a_side ";
        garbledCircuitTest(circuitFile,serverInputFile,clientInputFile);
    }

    private static void garbledCircuitTest(String circuitFile, String serverInputFile, String clientInputFile) throws IOException {
        ProcessBuilder gcProcess = new ProcessBuilder("/home/yi/Workspace/GCParser/runtestgcparser",   "/home/yi/Workspace/GCParser/b_side",  "/home/yi/Workspace/GCParser/a_side");
        gcProcess.redirectErrorStream(true);
        Process p = gcProcess.start();

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
