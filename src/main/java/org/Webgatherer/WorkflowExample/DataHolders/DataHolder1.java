package org.Webgatherer.WorkflowExample.DataHolders;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Rick Dane
 */
public class DataHolder1 {

    private LinkedList<String> emailAddresses = new LinkedList<String>();
    private Map<String, String> contentHolder = new HashMap<String, String>();

    public void addEmailAddress(String email) {
        emailAddresses.add(email);
    }

    public LinkedList<String> getEmailAddresses() {
        return emailAddresses;
    }

    public void addContent(String key, String content) {
        contentHolder.put(key, content);
    }

    public Map<String, String> getContentHolder() {
        return contentHolder;
    }
}
