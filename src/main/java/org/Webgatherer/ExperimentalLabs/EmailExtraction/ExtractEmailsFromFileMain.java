package org.Webgatherer.ExperimentalLabs.EmailExtraction;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunicationBase;
import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.Webgatherer.ExperimentalLabs.DependencyInjection.DependencyBindingModule;
import org.Webgatherer.Persistence.InputOutput.PersistenceImpl_WriteToFile;
import org.Webgatherer.Persistence.InputOutput.WriterOutputQueueToFile;
import org.Webgatherer.Utility.ReadFiles;
import org.Webgatherer.WorkflowExample.Workflows.Implementations.WebGatherer.EnumUrlRetrieveOptions;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Rick Dane
 */
public class ExtractEmailsFromFileMain {

    private static final String inputFilePath = "/home/user/Dropbox/Rick/WebGatherer/Output/google/searchResultLinks.txt";
    private static final String outputFilePath = "/home/user/Dropbox/Rick/WebGatherer/Output/extractedEmails.txt";
    private static ReadFiles readFiles;
    private static int sizeOfStringArrayEnum = 9;

    private static WriterOutputQueueToFile writerOutputQueueToFile;

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new DependencyBindingModule());

        PageRetrieverThreadManagerEmailExtraction pageRetrieverThreadManager = injector.getInstance(PageRetrieverThreadManagerEmailExtraction.class);

        ThreadCommunication threadCommunication = injector.getInstance(ThreadCommunication.class);
        pageRetrieverThreadManager.configure(threadCommunication);

        readFiles = injector.getInstance(ReadFiles.class);

        writerOutputQueueToFile = injector.getInstance(WriterOutputQueueToFile.class);

        threadCommunication.setPageQueue(prepareQueue());


        while (!threadCommunication.isPageQueueEmpty()) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
            }
            pageRetrieverThreadManager.run(EnumUrlRetrieveOptions.HTMLPAGE.ordinal());
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }


        writerOutputQueueToFile.writeQueueToFile(outputFilePath, threadCommunication, ThreadCommunicationBase.PageQueueEntries.EMAIL_ADDRESSES.ordinal(), true, ",");

    }


    private static Queue<String> prepareQueue() {
        List<String> urls = readFiles.readLinesToList(inputFilePath);

        Queue queue = new ConcurrentLinkedQueue<String>();

        for (String curUrl : urls) {
            String[] testEntry = new String[sizeOfStringArrayEnum];
            testEntry[ThreadCommunicationBase.PageQueueEntries.BASE_URL.ordinal()] = curUrl;
            queue.add(testEntry);
        }
        return queue;
    }
}
