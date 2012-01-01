package org.Webgatherer.WorkflowExample.Workflows.Implementations.DataInterpetor;

import com.google.inject.Injector;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.FinalOutputContainer;
import org.Webgatherer.CoreEngine.Core.ThreadCommunication.ThreadCommunication;
import org.Webgatherer.CoreEngine.Core.Threadable.DataInterpreatation.DataInterpretor;
import org.Webgatherer.ExperimentalLabs.HtmlProcessing.HtmlParserImpl;
import org.Webgatherer.WorkflowExample.DataHolders.ContainerBase;
import org.Webgatherer.WorkflowExample.DataHolders.DataHolderImpl;
import org.Webgatherer.WorkflowExample.Status.StatusIndicator;
import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.TextExtraction;
import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.Workflow_DataInterpretorBase;
import org.htmlcleaner.HtmlCleaner;
import sun.plugin.dom.core.Text;

import java.util.*;

/**
 * @author Rick Dane
 */
public final class Workflow_DataInterpretor_1 extends Workflow_DataInterpretorBase {

    private TextExtraction textExtraction;

    protected int count = 1;

    public Workflow_DataInterpretor_1(Injector injector) {
        super(injector);
        htmlParser = new HtmlParserImpl(htmlCleaner);
        textExtraction = injector.getInstance(TextExtraction.class);
        htmlCleanerProvider = injector.getProvider(HtmlCleaner.class);

    }


    @Override
    public void runWorkflow(Map<String, Object> workflowParams) {

        System.out.print(count + ", ");
        count++;

        runWorkflowSetup(workflowParams);

        if (curScrapedPage != null) {

            String[] checkFor1 = {"career", "job", "employment", "work"};
            checkForMatchesToSendBackLink(checkFor1, "careers");

            String[] checkFor3 = {"about", "info"};
            checkForMatchesToSendBackLink(checkFor3, "aboutus");

            String[] checkFor4 = {"site map"};
            checkForMatchesToSendBackLink(checkFor4, "sitemap");

            if (curCategory != null && (curCategory.equals("aboutus") || curCategory.equals("sitemap"))) {
                checkForMatchesToSendBackLink(checkFor1, "careers");
            }

            if (curCategory == null) {
                //add negative matches from the initial page, to be used to determine unique links on specific pages later
                textExtraction.extractAllLinksFromSameSite(this, curScrapedPage, "careers", curPageBaseDomainUrl, TextExtraction.LinkMatchType.NEGATIVE_MATCH);
            }
        }

        dataHolder = trie.get(curEntryKey);

        if (curCategory != null && curCategory.equals("aboutus")) {
            addPageToDataHolder("aboutus", curPageBaseUrl);
        }

        if (curCategory != null && curCategory.equals("careers")) {
            textExtraction.extractAllLinksFromSameSite(this, curScrapedPage, "careers", curPageBaseDomainUrl, TextExtraction.LinkMatchType.POSITIVE_MATCH);

            String[] mustContainAtLeastOne = {"developer", "engineer", "programmer"};
            String[] mustContainAllEntries = {"java", "software"};

            boolean isMatch = determineIfPageContains(mustContainAtLeastOne, mustContainAllEntries, curWebPageText);
            if (isMatch) {
                addPageToDataHolder("careers", curPageBaseUrl);
            }
        }

        //move any finished containers to the finished queue
        if (dataHolder != null && !dataHolder.isFinishedContainerQueueEmpty()) {
            addToFinalOutputContainer();
        }
    }

    protected void checkForMatchesToSendBackLink(String[] matches, String label) {
        LinkedList<String> tokenstoCheckFor = new LinkedList<String>();
        for (String curMatch : matches) {
            tokenstoCheckFor.add(curMatch);
        }

        textExtraction.extractLinksForSendbackThatMatchKeys(this, tokenstoCheckFor, curScrapedPage, label, curPageBaseDomainUrl);
    }

    @Override
    public void destroyCleanly() {
        while (!threadCommunication.isPageQueueEmpty()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        dataHolder.destroyRetrieveFinalData();
        addToFinalOutputContainer();
    }
}
