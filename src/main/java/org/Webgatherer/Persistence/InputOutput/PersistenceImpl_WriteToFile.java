package org.Webgatherer.Persistence.InputOutput;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collection;

/**
 * @author Rick Dane
 */
public class PersistenceImpl_WriteToFile implements Persistence {

    public void persistFlatData(Collection inputData, String persistenceTarget) {

    }

    private void writeToFile(String filePath, String text) {
        try {
            // Create file
            FileWriter fstream = new FileWriter(filePath);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(text);

            out.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
}
