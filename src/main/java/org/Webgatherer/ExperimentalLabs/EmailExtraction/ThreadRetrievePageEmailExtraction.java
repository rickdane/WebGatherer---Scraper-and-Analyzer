package org.Webgatherer.ExperimentalLabs.EmailExtraction;

import com.google.inject.Inject;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunicationBase;
import org.Webgatherer.CoreEngine.Core.Threadable.WebGather.ThreadRetrievePage;
import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParser;
import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.EmailExtractor;
import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.TextExtraction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.LinkedList;
import java.util.Set;

/**
 * @author Rick Dane
 */
public class ThreadRetrievePageEmailExtraction extends ThreadRetrievePage {

    protected EmailExtractor emailExtractor;
    private static String delimeter = "#";
    private static String delimeter2nd = "~";

    @Inject
    public ThreadRetrievePageEmailExtraction(WebDriverFactory webDriverFactory, TextExtraction textExtraction, HtmlParser htmlParser, EmailExtractor emailExtractor) {
        super(webDriverFactory, textExtraction, htmlParser);
        this.emailExtractor = emailExtractor;
    }

    @Override
    protected void getPage() {
        System.out.println("thread attempting to get page");
        driver.get(entry[ThreadCommunicationBase.PageQueueEntries.BASE_URL.ordinal()]);

        Set<String> strSet = driver.getWindowHandles();

        String page = driver.getPageSource();
        String curUrl = driver.getCurrentUrl();

        LinkedList<String> emailAddresses = emailExtractor.extractEmailAddressesList(page);
        StringBuilder emailsStrb = new StringBuilder();
        if (!emailAddresses.isEmpty()) {
            for (String email : emailAddresses) {
                emailsStrb.append(email + ",");
            }
        }
        entry[ThreadCommunicationBase.PageQueueEntries.EMAIL_ADDRESSES.ordinal()] = emailsStrb.toString();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("url" + delimeter2nd + curUrl + delimeter);
        stringBuilder.append("title" + delimeter2nd + driver.getTitle() + delimeter);

        entry[ThreadCommunicationBase.PageQueueEntries.CUSTOM_RET_VALUE.ordinal()] = stringBuilder.toString();

        threadCommunication.addToOutputDataHolder(entry);

    }

    public static String getDelimeter() {
        return delimeter;
    }

    public static String getDelimeter2nd() {
        return delimeter2nd;
    }

    @Override
    protected boolean actionIfUrlValid
            () {
        return true;
    }
}
