package io;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class CreateInputFile {
    String path = "/home/yi/Workspace/CFQ/GCParser/";
    String inputFile ;

    public CreateInputFile(String inputFile) {
        this.inputFile = path + inputFile;
    }

    public CreateInputFile(String pathString, String inputFile){
        this.path = pathString;
        this.inputFile = path + inputFile;
    }

    public void setClientVar(BigInteger bigInt) throws Exception{
        PrintWriter pw = new PrintWriter(inputFile);
        pw.println("a " + bigInt);
        pw.close();
    }

    public void setSeverVar(BigInteger bigInt) throws Exception{
        PrintWriter pw = new PrintWriter(inputFile);
        pw.println("b " + bigInt);
        pw.close();
    }


}
