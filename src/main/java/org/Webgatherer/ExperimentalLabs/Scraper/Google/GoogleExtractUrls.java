package org.Webgatherer.ExperimentalLabs.Scraper.Google;

import com.google.inject.Inject;
import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParser;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Rick Dane
 */
public class GoogleExtractUrls {

    private HtmlParser htmlParser;
    private String searchPhrase;

    private WebDriver driver;
    private int numPages;

    private int googleNum;
    private int basePageNum = 10;
    private int incrementNum = 10;
    private int delay = 1000;
    private int numResultsPerPage = 10;

    private String negativeMatchLinkPrefix = "google.com";
    private String negativeMatchLinkContains = "google";
    private String positiveMatchLinkPrefix = "http";

    private List<String> outputLinks = new ArrayList<String>();
    private List<String> duplicateCheckList = new ArrayList<String>();

    @Inject
    public GoogleExtractUrls(HtmlParser htmlParser, WebDriverFactory webDriverFactory) {
        this.htmlParser = htmlParser;
        driver = webDriverFactory.createNewWebDriver();
    }

    public void configure(String searchPhrase, int numPages) {
        this.searchPhrase = searchPhrase;
        this.numPages = numPages;
        googleNum = numPages * numResultsPerPage;
    }

    public List<String> extractUrls() {
        int i = basePageNum;
        while (i <= googleNum) {
            driver.get("https://www.google.com/search?q=" + searchPhrase + "&hl=en&num=" + i + "&lr=&ft=i&cr=&safe=images&tbs=qdr:m#q=livermore+java+developer&hl=en&lr=&tbs=qdr:m&prmd=imvns");

            Map<String, String> links = htmlParser.extractLinks(negativeMatchLinkPrefix, driver.getPageSource());

            addToOutputList(links);

            i = i + incrementNum;

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return outputLinks;
    }

    private void addToOutputList(Map<String, String> links) {
        for (Map.Entry<String, String> entries : links.entrySet()) {
            String url = entries.getValue();
            if (!url.startsWith(negativeMatchLinkPrefix) && !url.contains(negativeMatchLinkContains) && url.startsWith(positiveMatchLinkPrefix) && !duplicateCheckList.contains(url)) {
                outputLinks.add(url);
                duplicateCheckList.add(url);
            }
        }
    }
}
