package org.Webgatherer.ExperimentalLabs.DependencyInjection;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunicationImpl;
import org.Webgatherer.CoreEngine.Core.Threadable.WebGather.PageRetrieverThreadManager;
import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.Webgatherer.ExperimentalLabs.EmailExtraction.PageRetrieverThreadManagerEmailExtraction;
import org.Webgatherer.ExperimentalLabs.EmailExtraction.ThreadRetrievePageEmailExtraction;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParser;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParserImpl;
import org.Webgatherer.ExperimentalLabs.Mail.SendEmail;
import org.Webgatherer.ExperimentalLabs.Scraper.Core.PageRetrieverThreadManagerScraper;
import org.Webgatherer.ExperimentalLabs.Scraper.Generic.ScraperGeneric;
import org.Webgatherer.ExperimentalLabs.Scraper.Google.GoogleExtractUrls;
import org.Webgatherer.ExperimentalLabs.Scraper.Indeed.ScraperIndeed;
import org.Webgatherer.ExperimentalLabs.Scraper.Indeed.ThreadRetrievePageIndeed;
import org.Webgatherer.Persistence.InputOutput.PersistenceImpl_WriteToFile;
import org.Webgatherer.Persistence.InputOutput.WriterOutputQueueToFile;
import org.Webgatherer.Utility.RandomSelector;
import org.Webgatherer.Utility.ReadFiles;
import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.EmailExtractor;

/**
 * @author Rick Dane
 */
public class DependencyBindingModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(HtmlParser.class).to(HtmlParserImpl.class).in(Singleton.class);
        bind(WebDriverFactory.class).in(Singleton.class);
        bind(PageRetrieverThreadManager.class).in(Singleton.class);
        bind(PageRetrieverThreadManagerScraper.class).in(Singleton.class);
        bind(ThreadCommunication.class).to(ThreadCommunicationImpl.class);
        bind(EmailExtractor.class);
        bind(ThreadRetrievePageIndeed.class);
        bind(SendEmail.class);
        bind(ReadFiles.class);
        bind(PersistenceImpl_WriteToFile.class);
        bind(RandomSelector.class).in(Singleton.class);
        bind(GoogleExtractUrls.class);
        bind(ThreadRetrievePageEmailExtraction.class);
        bind(PageRetrieverThreadManagerEmailExtraction.class).in(Singleton.class);
        bind(WriterOutputQueueToFile.class).in(Singleton.class);
        bind(ScraperIndeed.class);
        bind(ScraperGeneric.class);
    }
}
