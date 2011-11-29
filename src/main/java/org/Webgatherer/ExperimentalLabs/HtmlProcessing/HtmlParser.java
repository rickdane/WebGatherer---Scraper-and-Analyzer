package org.Webgatherer.ExperimentalLabs.HtmlProcessing;

import java.util.List;

/**
 * @author Rick Dane
 */
public interface HtmlParser {

    public List<String> extractLinks(String htmlPage);

    public String getText(String htmlPage);

    public List<String> extractEmailAddresses(String htmlPage);
}
