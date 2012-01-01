package org.Webgatherer.ExperimentalLabs.HtmlProcessing;

import com.google.inject.Inject;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.util.HashMap;
import java.util.Map;

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
                url = getRelativeLink(url, baseUrl);

                urlList.put(curNode.getText().toString().toLowerCase().trim(), url);
            }
        }
        return urlList;
    }

    private String getRelativeLink(String url, String baseUrl) {
        String origUrl = url;
        if (!url.contains("http") && !url.contains("www")) {
            int urlLength = baseUrl.length();
            try {
                String checkForSlash = baseUrl.substring(urlLength - 1, urlLength);
                if (!checkForSlash.equals("/") && url.indexOf("/") != 0) {
                    url = "/" + url;
                }
                if (checkForSlash.equals("/") && url.indexOf("/") == 0) {
                    url = url.substring(1, url.length());
                }
                url = baseUrl + url;
            } catch (Exception e) {
                return origUrl;
            }
        }
        return url;
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
