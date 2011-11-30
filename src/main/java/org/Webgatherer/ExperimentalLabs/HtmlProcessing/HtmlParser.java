package org.Webgatherer.ExperimentalLabs.HtmlProcessing;

import java.util.List;
import java.util.Map;

/**
 * @author Rick Dane
 */
public interface HtmlParser {

    public Map<String, String> extractLinks(String baseUrl, String htmlPage);

    public String getText(String htmlPage);

    public List<String> extractEmailAddresses(String htmlPage);
}
