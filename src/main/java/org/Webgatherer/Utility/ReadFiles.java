package org.Webgatherer.Utility;

import java.io.*;
import java.util.*;

/**
 * This class is meant for reading text files Lists, similar to a properties file
 * The purpose of this is to provide a way for workflows to use data from text files rather than having it coded directly into a Java class
 *
 * @author Rick Dane
 */
public class ReadFiles {

    private String filesLocation = "/home/user/Dropbox/Rick/WebGatherer/WebGatherer---Scraper-and-Analyzer/src/main/resources/filesLoadIntoHashmap";
    private FileReader fr;
    private BufferedReader br;
    private HashMap<String, List<String>> fileLists = new HashMap<String, List<String>>();

    public ReadFiles() {
        readFilesToHashMap(filesLocation);
    }

    public List<String> getListByKey(String fileNameKey) {
        return fileLists.get(fileNameKey);
    }


    public void readFilesToHashMap(String folderPath) {

        String fileName;
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {
                fileName = listOfFiles[i].getName();

                List<String> lineList = readLinesToList(filesLocation + "/" + fileName);
                fileLists.put(fileName, lineList);
            }
        }
    }

    public List<String> readLinesToList(String filePath) {
        List<String> linesList = new ArrayList<String>();

        try {
            FileInputStream fstream = new FileInputStream(filePath);

            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                linesList.add(strLine);
            }
            in.close();
        } catch (Exception e) {//Catch exception if any
            e.printStackTrace();
            return null;
        }
        return linesList;
    }
}
