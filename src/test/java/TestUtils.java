import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestUtils {
    public static void compareCSV(String expected, String actual) {
        try {
            List<String[]> expectedData = readCSV(expected);
            List<String[]> actualData = readCSV(actual);
            assertEquals(expectedData.size(), actualData.size(), "row number is different");
            for (int i = 0; i < expectedData.size(); i++) {
                String[] expectedRow = expectedData.get(i);
                String[] actualRow = actualData.get(i);

                assertEquals(expectedRow.length, actualRow.length, "column number is different");
                for (int j = 0; j < expectedRow.length; j++) {
                    assertEquals(expectedRow[j], actualRow[j], "column " + (j+1) + " is different");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String[]> readCSV(String filename) throws IOException {
        List<String> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            data.add(line);
        }
        reader.close();
        Collections.sort(data);
        List<String[]> res = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            String[] lineSplit = data.get(i).split(",");
            for(int j = 0; j < lineSplit.length; j++) {
                lineSplit[j] = lineSplit[j].trim();
            }
            res.add(lineSplit);
        }
        return res;
    }
}
