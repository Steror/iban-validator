package ibanvalidator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileIO {

    private static final String DIRECTORY = System.getProperty("user.home"); // Default file location
    private final String absoluteInputPath;
    private final String absoluteOutputPath;

    // Initialize path when creating class
    public FileIO(String path) {
        absoluteInputPath = path;
        absoluteOutputPath = absoluteInputPath.replace(".txt", ".out");
    }
    // Read IBAN list from file
    public List<String> readIban() {
        List<String> ibanList = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(absoluteInputPath))) {
            System.out.println("Reading.....");
            String line = bufferedReader.readLine();
            while(line != null) {
                System.out.println("Read: " + line);
                ibanList.add(line);
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found at: " + DIRECTORY + System.lineSeparator() + e); // Print exception message
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Print exception
        }
        return ibanList;
    }
    // Write IBAN list results to .out file
    public boolean writeIban(final Map<String, Boolean> ibanResult) {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(absoluteOutputPath))) {
            System.out.println("Writing.....");
            for (String key: ibanResult.keySet()) { // LinkedHashMap is much faster to iterate through apparently
                System.out.println("Write: " + key + ";" + ibanResult.get(key));
                bufferedWriter.write(key + ";" + ibanResult.get(key) + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Print exception
            return false;
        }
        return true;
    }
}
