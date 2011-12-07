package org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor;

import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.Workflow_DataInterpretorBase;

import java.util.LinkedList;
import java.util.Map;

/**
 * @author Rick Dane
 */
public class TextExtraction {

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

            ifNotUsedAdd(instance, url, internalLabel, curLinkLabel);
        }
    }

    //TODO This method was done hastily as its mostly copy pasted from the similar method above, refactor both so they use common private methods for shared logic
    public void extractAllLinksFromSameSite(Workflow_DataInterpretorBase instance, String parsedHtml, String internalLabel, String curDomainName) {

        Map<String, String> links = instance.htmlParser.extractLinks(instance.curPageBaseUrl, parsedHtml);

        for (Map.Entry<String, String> entry : links.entrySet()) {
            String curLinkLabel = entry.getKey();
            String url = entry.getValue();

            if (!url.toLowerCase().contains(curDomainName.toLowerCase())) {
                // it's not a link from the original site so we don't add it, TODO: may want to make this an optional parameter at some point
                continue;
            }

            ifNotUsedAdd(instance, url, internalLabel, curLinkLabel);

        }
    }

    private void ifNotUsedAdd(Workflow_DataInterpretorBase instance, String url, String internalLabel, String curLinkLabel) {
        if (!instance.trackSentBackLinks.contains(curLinkLabel)) {
            String[] strHolder = {instance.curEntryKey, url, internalLabel, null};
            instance.threadCommunication.addToSendbackDataHolder(strHolder);
            instance.trackSentBackLinks.add(curLinkLabel);
        }
    }

    public void extractEmailAddressesToDataHolder(Workflow_DataInterpretorBase instance, String parsedHtml, String internalLabel) {

        Map<String, String> links = instance.htmlParser.extractLinks(instance.curPageBaseUrl, parsedHtml);


    }

}
