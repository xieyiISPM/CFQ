package gc;

public class GarbledCircuit {
    private String circuitFile = "/home/yi/Workspace/GCParser/cmp.cir";
    private String serverInputFile = " /home/yi/Workspace/GCParser/b_side ";
    private String clientInputFile = " /home/yi/Workspace/GCParser/a_side ";

    public GarbledCircuit(String circuitFile, String serverInputFile, String clientInputFile){
        this.circuitFile = circuitFile;
        this.serverInputFile = serverInputFile;
        this.clientInputFile = clientInputFile;
    }

    public GarbledCircuit(){

    }


    public int add_cmp(String serverOutputFile, String clientOutputFile){
        //todo read outpufile
        return 1;
    }
}
