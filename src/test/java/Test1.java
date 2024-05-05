import org.example.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Test1 {

    String resourcePath = null;

    @BeforeEach
    public void setUp() {
        try {
            resourcePath = Paths.get(getClass().getClassLoader().getResource("cases").toURI()).toString();
//            resourcePath = Paths.get(getClass().getClassLoader().getResource("mycases-iter3").toURI()).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void test() {
        String realOutput = resourcePath + System.getProperty("file.separator") + "output.csv";
        String expected = resourcePath + System.getProperty("file.separator") + "output" + System.getProperty("file.separator") + "expected.csv";
        String[] args = new String[]{
                resourcePath,
                realOutput
        };
        Main.main(args);
        TestUtils.compareCSV(expected, realOutput);
    }

    @Test
    public void testComplexity() {
        String realOutput = resourcePath + System.getProperty("file.separator") + "output_complexity.csv";
        String expected = resourcePath + System.getProperty("file.separator") + "output" + System.getProperty("file.separator") + "complexity.csv";
        String[] args = new String[]{
                resourcePath,
                realOutput
        };
        Main.main(args);
        TestUtils.compareCSV(expected, realOutput);
    }
}

