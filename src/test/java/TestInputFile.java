import io.CreateADDCMPInputFile;

import java.math.BigInteger;

public class TestInputFile {
    public static void main(String[] args) throws Exception {
        CreateADDCMPInputFile ciA = new CreateADDCMPInputFile("a_side");
        CreateADDCMPInputFile ciB = new CreateADDCMPInputFile("b_side");
        ciA.setClientVar(BigInteger.valueOf(150), BigInteger.valueOf(22));
        ciB.setSeverVar(BigInteger.valueOf(120), BigInteger.valueOf(55));
    }
}
