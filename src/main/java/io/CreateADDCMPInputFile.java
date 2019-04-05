package io;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class CreateADDCMPInputFile {
    String path = "/home/yi/Workspace/CFQ/GCParser/";
    String inputFile ;

    public CreateADDCMPInputFile(String inputFile) {
        this.inputFile = path + inputFile;
    }

    public CreateADDCMPInputFile(String pathString, String inputFile){
        this.path = pathString;
        this.inputFile = path + inputFile;
    }

    public void setClientVar(BigInteger bigIntA1, BigInteger bigIntB1) throws Exception{
        PrintWriter pw = new PrintWriter(inputFile);
        pw.println("a1 " + bigIntA1);
        pw.println("b1 " + bigIntB1);
        pw.close();
    }

    public void setSeverVar(BigInteger bigIntA2, BigInteger bigIntB2) throws Exception{
        PrintWriter pw = new PrintWriter(inputFile);
        pw.println("a2 " + bigIntA2);
        pw.println("b2 " + bigIntB2);

        pw.close();
    }


}
