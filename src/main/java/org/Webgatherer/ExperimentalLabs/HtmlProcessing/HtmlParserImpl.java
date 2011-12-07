package org.Webgatherer.ExperimentalLabs.HtmlProcessing;

import com.google.inject.Inject;
import org.htmlcleaner.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rick Dane
 */
public class HtmlParserImpl implements HtmlParser {

    private HtmlCleaner htmlCleaner;
    private CleanerProperties htmlCleanerProperties;

    @Inject
    public HtmlParserImpl(HtmlCleaner htmlCleaner) {
        this.htmlCleaner = htmlCleaner;
        htmlCleanerProperties = htmlCleaner.getProperties();
    }

    public Map<String, String> extractLinks(String baseUrl, String htmlPage) {
        TagNode node = htmlCleaner.clean(htmlPage);

        TagNode[] nodesHref = node.getElementsByName("a", true);
        Map<String, String> urlList = new HashMap<String, String>();

        for (TagNode curNode : nodesHref) {
            Map<String, String> attributes = curNode.getAttributes();
            if (attributes.containsKey("href")) {
                String url = curNode.getAttributeByName("href").trim();
                if (!url.contains("http") && !url.contains("www")) {
                    int urlLength = baseUrl.length();
                    String checkForSlash = baseUrl.substring(urlLength - 1, urlLength);
                    if (!checkForSlash.equals("/") && url.indexOf("/") != 0) {
                        url = "/" + url;
                    }
                    if (checkForSlash.equals("/") && url.indexOf("/") == 0) {
                        url = url.substring(1, url.length());
                    }
                    url = baseUrl + url;
                }
                urlList.put(curNode.getText().toString().toLowerCase().trim(), url);
            }
        }
        return urlList;
    }

    public String getText(String htmlPage) {
        TagNode node = htmlCleaner.clean(htmlPage);
        StringBuffer stringBuffer = node.getText();
        return stringBuffer.toString();
    }

    private void defaultConfigHtmlCleaner() {
        htmlCleanerProperties.setOmitComments(true);
        htmlCleanerProperties.setTreatUnknownTagsAsContent(true);
    }

}
