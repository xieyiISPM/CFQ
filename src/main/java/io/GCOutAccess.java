package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class GCOutAccess {
    String fileName;
    public GCOutAccess(String fileName){
        this.fileName = fileName;
    }


    public int readResult(){
        try {
            Stream<String> lines = Files.lines(Paths.get(fileName));
            String line10 =lines.skip(9).findFirst().get();
            //System.out.println(line10);
            String delims="[ ]+";

            String[] token = line10.split(delims);
            return Integer.parseInt(token[2]);
        }
        catch(IOException ioe){
            ioe.printStackTrace();
            System.out.println(ioe);
        }
        return -1;
    }

}
