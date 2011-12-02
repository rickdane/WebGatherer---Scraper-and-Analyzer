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
    public void extractLinksForSendbackThatMatchKeys(Workflow_DataInterpretorBase instance, LinkedList<String> keys, String parsedHtml, String internalLabel) {

        Map<String, String> links = instance.htmlParser.extractLinks(instance.curPageBaseUrl, parsedHtml);

        for (Map.Entry<String, String> entry : links.entrySet()) {
            String curLinkLabel = entry.getKey();
            if (!keys.contains(curLinkLabel.toLowerCase())) {
                continue;
            }
            String url = entry.getValue();
            if (!instance.trackSentBackLinks.contains(curLinkLabel)) {
                String[] strHolder = {instance.curEntryKey, url, internalLabel, null};
                instance.threadCommunication.addToSendbackDataHolder(strHolder);
                instance.trackSentBackLinks.add(curLinkLabel);
            }
        }
    }


    public void extractEmailAddressesToDataHolder(Workflow_DataInterpretorBase instance, String parsedHtml, String internalLabel) {

        Map<String, String> links = instance.htmlParser.extractLinks(instance.curPageBaseUrl, parsedHtml);


    }

}
