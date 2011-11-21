package org.Webgatherer.Core.ThreadCommunication;

import java.util.*;

/**
 * @author Rick Dane
 */
public class ThreadCommunicationImpl extends ThreadCommunicationBase implements ThreadCommunication {

    private volatile Queue<String> outputDataHolder = new LinkedList<String>();
    private volatile Queue<String> pageQueue = new LinkedList<String>();
    private volatile Queue<String> sendbackDataHolder = new LinkedList<String>();
    private volatile Map<String, Object> customDataMap = new HashMap<String, Object>();

    public void setPageQueue(Queue pageQueue) {
        this.pageQueue = pageQueue;
    }

    public boolean isOutputDataHolderEmpty() {
        if (outputDataHolder.isEmpty()) {
            return true;
        }
        return false;
    }

    public void addToOutputDataHolder(String string) {
        outputDataHolder.add(string);
    }

    public boolean isSendbackDataHolderEmpty() {
        if (sendbackDataHolder.isEmpty()) {
            return true;
        }
        return false;
    }

    public String getFromOutputDataHolder() {
        if (!isOutputDataHolderEmpty()) {
            return outputDataHolder.remove();
        }
        return null;
    }

    public String getFromSendbackDataHolder() {
        if (!isSendbackDataHolderEmpty()) {
            return sendbackDataHolder.remove();
        }
        return null;
    }

    public boolean isPageQueueEmpty() {
        if (pageQueue.isEmpty()) {
            return true;
        }
        return false;
    }

    public String getFromPageQueue() {
        if (!isPageQueueEmpty()) {
            return pageQueue.remove();
        }
        return null;
    }

    public void setCustomDataMapElement(String key, Object obj) {

    }

    public void addToSendbackDataHolder(String string) {
        sendbackDataHolder.add(string);
    }

    public Object getCustomDataMapElement(String key) {
        Object returnObject = null;
        try {
            returnObject = customDataMap.get(key);
        } catch (Exception e) {

        }
        return returnObject;
    }

    public Map<String, Object> getCustomDataMap() {
        synchronized (customDataMap) {
            return customDataMap;
        }
    }

    public void setCustomDataMap(Map map) {
        synchronized (customDataMap) {
            this.customDataMap = map;
        }

    }

    public void addToPageQueue(String page) {
        synchronized (pageQueue) {
            pageQueue.add(page);
        }
    }

}
