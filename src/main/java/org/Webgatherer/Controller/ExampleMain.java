package org.Webgatherer.Controller;

import org.Webgatherer.Utility.TextCleaner;

/**
 * @author Rick Dane
 */
public class ExampleMain {

    /**
     * FOR TESTING ONLY
     *
     * @param args
     */
    public static void main(String[] args) {

        String test = "something.com/kasfjk.html";
        System.out.println(prepareBaseDomainUrl(test));

    }

    private static String prepareBaseDomainUrl(String url) {
        int index = url.indexOf("/");

        if (index != -1) {
            url = url.substring(0, index);
        }
        return url;
    }
}
