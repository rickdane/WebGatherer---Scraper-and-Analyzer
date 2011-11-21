package org.Webgatherer.Utility.HtmlParsing;

import java.util.List;

/**
 * @author Rick Dane
 */
public interface HtmlParser {

    public List<String> extractLinks(String htmlPage);

    public String getText(String htmlPage);
}
