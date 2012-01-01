package org.Webgatherer.Controller;

import org.Webgatherer.Api.Scraper.ScraperFactory;
import org.Webgatherer.Controller.Base.EntryBase;
import org.Webgatherer.ExperimentalLabs.Scraper.Core.ScraperBase;

import java.util.List;

/**
 * @author Rick Dane
 */
public class Entry_ScraperGeneric extends EntryBase {

    public static void main(String[] args) {

        int pageNum = 1;
        int maxPages = 2;

        ScraperBase scraper = ScraperFactory.createScraper("generic");

        List<String[]> urlEntries = scraper.run("java", pageNum, maxPages);

        //demonstration of persisting to web service
        for (String[] curEntry : urlEntries) {
            persistEntry(curEntry[1]);
        }
    }
}
