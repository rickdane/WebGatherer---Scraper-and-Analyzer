package org.Webgatherer.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * This is just an ad-hoc class at this point
 *
 * @author Rick Dane
 */
public class TextReformatter {

    private static String inputFilePath = "/home/user/Desktop/rawids";
    private static String outputFilePath = "/home/user/Desktop/formattedforsql";
    private static String surroundingCharacter = "";    //"\""

    public static void main(String[] args) {


        ReadFiles readFiles = new ReadFiles();
        List<String> linesList = readFiles.readLinesToList(inputFilePath);

        List<String> duplicateCheck = new ArrayList<String>();

        StringBuilder strBld = new StringBuilder();
        for (String curLine : linesList) {

            strBld.append(curLine);

        }

        StringBuilder outputStringBld = new StringBuilder();

        String document = strBld.toString();

        String[] split = document.split("\\|");

        for (String curEntry : split) {
            String[] splitComma = curEntry.split(",");
            outputStringBld.append(splitComma[0] + ",\n");
        }

        System.out.println(outputStringBld.toString());

    }

}
