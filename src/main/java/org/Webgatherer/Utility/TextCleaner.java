package org.Webgatherer.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rick Dane
 */
public class TextCleaner {

    public String removeUrlPrefix(String inputStr) {
        List<String> matchList = new ArrayList<String>();
        matchList.add("http");
        matchList.add("www");

        boolean isMatch = false;

        for (String curEntry : matchList) {
            if (inputStr.contains(inputStr)) {
                isMatch = true;
            }
        }

        String cleanedString = "";

        if (isMatch) {
            String matchAgainst1 = "www.";
            int indx = inputStr.indexOf(matchAgainst1);
            if (indx >= 0) {
                return inputStr.substring(indx + matchAgainst1.length(), inputStr.length());
            }
            String matchAgainst2 = "http://";
            indx = inputStr.indexOf(matchAgainst2);
            if (indx >= 0) {
                return inputStr.substring(indx + matchAgainst2.length(), inputStr.length());
            }

        } else {
            return inputStr;
        }
        return inputStr;
    }
}
