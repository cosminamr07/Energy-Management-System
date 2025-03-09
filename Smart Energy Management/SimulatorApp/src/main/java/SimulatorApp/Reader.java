package SimulatorApp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reader {

    public static List<Double> readSensorData(String filePath) {
        List<Double> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    data.add(Double.parseDouble(line.trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid data in CSV: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
