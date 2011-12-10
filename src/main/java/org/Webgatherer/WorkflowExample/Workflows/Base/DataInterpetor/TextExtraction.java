package org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor;

import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.Workflow_DataInterpretorBase;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

/**
 * @author Rick Dane
 */
public class TextExtraction {
    private HashSet<String> ignoreSuffixes;
    private HashSet<String> negativeContains;

    public TextExtraction() {
        PrepareNegativeMatchLists();
    }

    private void PrepareNegativeMatchLists() {
        ignoreSuffixes = new HashSet<String>();
        ignoreSuffixes.add(".pdf");
        ignoreSuffixes.add(".txt");
        ignoreSuffixes.add(".zip");
        ignoreSuffixes.add(".js");
        ignoreSuffixes.add(".javascript");
        ignoreSuffixes.add(".css");
        ignoreSuffixes.add(".doc");
        ignoreSuffixes.add(".jpg");
        ignoreSuffixes.add(".gif");
        ignoreSuffixes.add(".png");
        ignoreSuffixes.add(".bmp");
        ignoreSuffixes.add(".xls");

        negativeContains = new HashSet<String>();
        negativeContains.add("@");
    }

    /**
     * Extracts links from a page that match one from the list passed in, sends to sendback object with specified internal label
     */
    public void extractLinksForSendbackThatMatchKeys(Workflow_DataInterpretorBase instance, LinkedList<String> tokens, String parsedHtml, String internalLabel, String curDomainName) {

        Map<String, String> links = instance.htmlParser.extractLinks(instance.curPageBaseDomainUrl, parsedHtml);

        for (Map.Entry<String, String> entry : links.entrySet()) {
            String curLinkLabel = entry.getKey();
            String url = entry.getValue();

            if (!url.toLowerCase().contains(curDomainName.toLowerCase())) {
                // it's not a link from the original site so we don't add it, TODO: may want to make this an optional parameter at some point
                continue;
            }

            boolean isMatch = false;
            for (String curToken : tokens) {
                if (curLinkLabel.toLowerCase().contains(curToken.toLowerCase()) || curLinkLabel.toLowerCase().contains(curToken.toLowerCase())) {
                    isMatch = true;
                    break;
                }
            }
            if (isMatch == false) {
                continue;
            }

            ifNotUsedAdd(instance, url, internalLabel, LinkMatchType.POSITIVE_MATCH);
        }
    }

    public enum LinkMatchType {
        POSITIVE_MATCH, NEGATIVE_MATCH;
    }

    //TODO This method was done hastily as its mostly copy pasted from the similar method above, refactor both so they use common private methods for shared logic
    public void extractAllLinksFromSameSite(Workflow_DataInterpretorBase instance, String parsedHtml, String internalLabel, String curDomainName, LinkMatchType linkMatchType) {

        Map<String, String> links = instance.htmlParser.extractLinks(instance.curPageBaseDomainUrl, parsedHtml);

        for (Map.Entry<String, String> entry : links.entrySet()) {
            String curLinkLabel = entry.getKey();
            String url = entry.getValue();

            if (!url.toLowerCase().contains(curDomainName.toLowerCase())) {
                // it's not a link from the original site so we don't add it, TODO: may want to make this an optional parameter at some point
                continue;
            }

            ifNotUsedAdd(instance, url, internalLabel, linkMatchType);

        }
    }

    private void ifNotUsedAdd(Workflow_DataInterpretorBase instance, String url, String internalLabel, LinkMatchType linkMatchType) {

        if (linkMatchType.equals(LinkMatchType.POSITIVE_MATCH)) {
            if (!isUrlValid(url)) {
                instance.negativeMatchUrlList.add(url);
            }
            if (!instance.trackSentBackLinks.contains(url) && !instance.negativeMatchUrlList.contains(url)) {
                String[] strHolder = {instance.curEntryKey, url, internalLabel, null};
                instance.threadCommunication.addToSendbackDataHolder(strHolder);
                instance.trackSentBackLinks.add(url);
            }
        } else if (linkMatchType.equals(LinkMatchType.NEGATIVE_MATCH)) {
            if (!instance.negativeMatchUrlList.contains(url) && !instance.trackSentBackLinks.contains(url)) {
                instance.negativeMatchUrlList.add(url);
            }
        }
    }

    public void extractEmailAddressesToDataHolder(Workflow_DataInterpretorBase instance, String parsedHtml, String internalLabel) {

        Map<String, String> links = instance.htmlParser.extractLinks(instance.curPageBaseUrl, parsedHtml);


    }

    /**
     * Runs a url through checks to determine if it appears to be a valid url for a web page
     *
     * @return
     */
    public boolean isUrlValid(String url) {
        for (String curIgnoreCheck : ignoreSuffixes) {
            if (url.endsWith(curIgnoreCheck)) {
                return false;
            }
        }
        for (String negativeMatch : negativeContains) {
            if (url.contains(negativeMatch)) {
                return false;
            }
        }
        return true;
    }
}
