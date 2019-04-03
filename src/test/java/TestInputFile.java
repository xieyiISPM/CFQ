import io.CreateInputFile;

import java.io.IOException;
import java.math.BigInteger;

public class TestInputFile {
    public static void main(String[] args) throws Exception {
        CreateInputFile ciA = new CreateInputFile("a_side");
        CreateInputFile ciB = new CreateInputFile("b_side");
        ciA.setClientVar(BigInteger.valueOf(150));
        ciB.setSeverVar(BigInteger.valueOf(120));
    }
}
