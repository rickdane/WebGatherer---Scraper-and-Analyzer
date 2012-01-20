package org.Webgatherer.Api.Scraper;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.Webgatherer.ExperimentalLabs.DependencyInjection.DependencyBindingModule;
import org.Webgatherer.ExperimentalLabs.Scraper.Core.ScraperBase;
import org.Webgatherer.ExperimentalLabs.Scraper.Generic.ScraperGeneric;
import org.Webgatherer.ExperimentalLabs.Scraper.Indeed.ScraperIndeed;

/**
 * @author Rick Dane
 */
public class ScraperFactory {

    public static ScraperBase createScraper(String scraperIdentifier) {

        Injector injector = Guice.createInjector(new DependencyBindingModule());

        //TODO: replace with reflection, just do it this way for ease in initial testing

        ScraperBase scraperBase = null;
        if (scraperIdentifier.equalsIgnoreCase("indeed")) {

            scraperBase = injector.getInstance(ScraperIndeed.class);

        } else if (scraperIdentifier.equalsIgnoreCase("generic")) {

            //THIS IS THE ONE TO USE IN MOST CASES AS WE ARE ADDING IN ABILITY TO PASS IN URL PATTERN FROM UI

            scraperBase = injector.getInstance(ScraperGeneric.class);
        }

        return scraperBase;
    }

}
