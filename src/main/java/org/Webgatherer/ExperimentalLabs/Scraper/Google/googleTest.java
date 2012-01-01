package org.Webgatherer.ExperimentalLabs.Scraper.Google;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.Webgatherer.ExperimentalLabs.DependencyInjection.DependencyBindingModule;
import org.Webgatherer.ExperimentalLabs.Scraper.Deprecated.ScraperBaseStatic;
import org.Webgatherer.Persistence.InputOutput.PersistenceImpl_WriteToFile;

import java.util.List;

/**
 * @author Rick Dane
 */
public class googleTest extends ScraperBaseStatic {

    private static final String outputFile = "/home/user/Dropbox/Rick/WebGatherer/Output/google/searchResultLinks.txt";
    private static int numPages = 15;

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new DependencyBindingModule());
        GoogleExtractUrls googleExtractUrls = injector.getInstance(GoogleExtractUrls.class);
        googleExtractUrls.configure("java+developer+san+francisco+site%3Acraigslist.org", numPages);
        List<String> retList = googleExtractUrls.extractUrls();

        StringBuilder strBld = new StringBuilder();

        for (String curEntry : retList) {
            strBld.append(curEntry + "\n");
        }

        PersistenceImpl_WriteToFile.appendToFile(outputFile, strBld.toString());
    }
}
