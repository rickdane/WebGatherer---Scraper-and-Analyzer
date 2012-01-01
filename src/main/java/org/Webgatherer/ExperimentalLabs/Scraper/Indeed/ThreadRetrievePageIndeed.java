package org.Webgatherer.ExperimentalLabs.Scraper.Indeed;

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
public class ThreadRetrievePageIndeed extends ThreadRetrievePage {

    protected EmailExtractor emailExtractor;
    private static String delimeter = "#";
    private static String delimeter2nd = "~";

    @Inject
    public ThreadRetrievePageIndeed(WebDriverFactory webDriverFactory, TextExtraction textExtraction, HtmlParser htmlParser, EmailExtractor emailExtractor) {
        super(webDriverFactory, textExtraction, htmlParser);
        this.emailExtractor = emailExtractor;
    }

    @Override
    protected void getPage() {
        driver.get(entry[ThreadCommunicationBase.PageQueueEntries.BASE_URL.ordinal()]);

        WebElement element = driver.findElement(By.xpath("//a[@href='" + entry[ThreadCommunicationBase.PageQueueEntries.CUSTOM_PARAM.ordinal()] + "']"));
        System.out.println("attempt click");
        element.click();

        Set<String> strSet = driver.getWindowHandles();
        for (String curWindow : strSet) {
            driver.switchTo().window(curWindow);

            String page = driver.getPageSource();

            String curUrl = driver.getCurrentUrl();
            if (!curUrl.contains("www.indeed")) {

                LinkedList<String> emailAddresses = emailExtractor.extractEmailAddressesList(page);
                StringBuilder emailsStrb = new StringBuilder();
                if (!emailAddresses.isEmpty()) {
                    for (String email : emailAddresses) {
                        emailsStrb.append(email + ",");
                    }
                }
                entry[ThreadCommunicationBase.PageQueueEntries.EMAIL_ADDRESSES.ordinal()] = emailsStrb.toString();

                //TODO: rethink this, we are doing this and then undoing it later, figure out why its here at all
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("url" + delimeter2nd + curUrl + delimeter);
                stringBuilder.append("title" + delimeter2nd + driver.getTitle() + delimeter);

                entry[ThreadCommunicationBase.PageQueueEntries.CUSTOM_RET_VALUE.ordinal()] = stringBuilder.toString();

                threadCommunication.addToOutputDataHolder(entry);
                System.out.println("thread added to data output");
            }
        }
    }

    @Override
    protected boolean actionIfUrlValid
            () {
        return true;
    }
}
