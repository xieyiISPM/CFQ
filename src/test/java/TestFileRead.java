import io.GCOutAccess;

public class TestFileRead {
    public static void main(String[] main){
        String clientFileName = "GCParser/results/siclientout";
        String serverFileName = "GCParser/results/siserverout";

        GCOutAccess gcClientOut = new GCOutAccess(clientFileName);
        GCOutAccess gcSeverOut = new GCOutAccess(serverFileName);
        System.out.println(gcClientOut.readResult());
        System.out.println(gcSeverOut.readResult());

    }
}
