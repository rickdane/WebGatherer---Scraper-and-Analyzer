package org.Webgatherer.Persistence.InputOutput;

import com.google.inject.Inject;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunicationBase;

import java.util.LinkedList;

/**
 * @author Rick Dane
 */
public class WriterOutputQueueToFile {

    private PersistenceImpl_WriteToFile persWriteToFile;
    private int minEntryLength = 1;
    private int minStringArryLength = 2;

    @Inject
    public WriterOutputQueueToFile(PersistenceImpl_WriteToFile persWriteToFile) {
        this.persWriteToFile = persWriteToFile;
    }


    public String writeQueueToFile(String outputFilePath, ThreadCommunication threadCommunication, int indexPageQueueEntries, boolean doSplit, String splitDelim) {

        LinkedList<String> duplicateCheck = new LinkedList<String>();

        StringBuilder strBld = new StringBuilder();
        while (!threadCommunication.isOutputDataHolderEmpty()) {
            String[] curEntry = threadCommunication.getFromOutputDataHolder();
            String writeEntry = curEntry[indexPageQueueEntries];

            boolean wroteDuplicate = false;

            if (doSplit) {
                String[] split = writeEntry.split(splitDelim);

                if (!(split.length >= minStringArryLength)) {
                    StringBuilder tmpStrBld = new StringBuilder();
                    for (String curEntr : split) {
                        wroteDuplicate = doAppendIfNotDuplicate(wroteDuplicate, duplicateCheck, strBld, curEntr + "\n");
                    }
                    continue;
                }
            }

            if (!(writeEntry.length() >= minEntryLength)) {
                continue;
            }

            doAppendIfNotDuplicate(wroteDuplicate, duplicateCheck, strBld, writeEntry + "\n");

        }

        persWriteToFile.writeToFile(outputFilePath, strBld.toString());

        return strBld.toString();
    }

    private boolean doAppendIfNotDuplicate(boolean wroteDuplicate, LinkedList<String> duplicateCheck, StringBuilder strBld, String appendStr) {
        if (!duplicateCheck.contains(appendStr)) {
            strBld.append(appendStr);
        }

        if (!wroteDuplicate) {
            duplicateCheck.add(appendStr);
        }

        return true;
    }

}
