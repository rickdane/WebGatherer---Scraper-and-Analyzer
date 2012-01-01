package org.Webgatherer.ExperimentalLabs.EmailExtraction;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.Webgatherer.Common.Properties.PropertiesContainer;
import org.Webgatherer.CoreEngine.Core.Threadable.WebGather.PageRetrieverThreadManager;
import org.Webgatherer.CoreEngine.Core.Threadable.WebGather.ThreadCommunicationPageRetriever;
import org.Webgatherer.CoreEngine.Core.Threadable.WebGather.ThreadRetrievePage;
import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.openqa.selenium.WebDriver;

/**
 * @author Rick Dane
 */
public class PageRetrieverThreadManagerEmailExtraction extends PageRetrieverThreadManager {

    protected WebDriver driver;
    protected int sizeOfStringArrayEnum = 9;
    protected Provider<ThreadRetrievePageEmailExtraction> threadRetrievePageProvider;

    @Inject
    public PageRetrieverThreadManagerEmailExtraction(Provider<ThreadRetrievePage> threadRetrievePageProvider, PropertiesContainer propertiesContainer, ThreadCommunicationPageRetriever threadCommunicationPageRetriever, WebDriverFactory webDriverFactory, Provider<ThreadRetrievePageEmailExtraction> threadRetrievePageIndeedProvider) {
        super(threadRetrievePageProvider, propertiesContainer, threadCommunicationPageRetriever);
        driver = webDriverFactory.createNewWebDriver();
        this.threadRetrievePageProvider = threadRetrievePageIndeedProvider;
    }

    @Override
    protected void launchThread(String[] entry, int retrieveType) {
        ThreadRetrievePageEmailExtraction threadRetrievePage = threadRetrievePageProvider.get();
        threadRetrievePage.configure(entry, threadCommunication, retrieveType, threadCommunicationPageRetriever);
        threadRetrievePage.start();
    }

    @Override
    protected boolean determineIfCanStartThreadImmediately(String[] entry) {
        return true;
    }

}
