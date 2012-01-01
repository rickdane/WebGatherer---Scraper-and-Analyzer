package org.Webgatherer.ExperimentalLabs.Mail;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.Webgatherer.CoreEngine.lib.WebDriverFactory;
import org.Webgatherer.ExperimentalLabs.DependencyInjection.DependencyBindingModule;
import org.Webgatherer.Persistence.InputOutput.Persistence;
import org.Webgatherer.Persistence.InputOutput.PersistenceImpl_WriteToFile;
import org.Webgatherer.Persistence.InputOutput.ReadFromFileToList;
import org.Webgatherer.Utility.RandomSelector;
import org.Webgatherer.Utility.ReadFiles;
import org.Webgatherer.WorkflowExample.Workflows.Base.DataInterpetor.EmailExtractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Rick Dane
 */
public class mainSendEmail {

    //"/home/user/Dropbox/Rick/WebGatherer/Output/backups/indeed.com/java-san-francisco";
    private static final String outputFilePath = "/home/user/Dropbox/Rick/WebGatherer/Output/processed/indeed/emailsSent.txt";
    private static final String notSentFilePath = "/home/user/Dropbox/Rick/WebGatherer/Output/processed/indeed/emailsNotSent.txt";
    private static final String messagesSubjectFolder = "/home/user/Dropbox/Rick/WebGatherer/Input/emails/jobApplication/subject";
    private static final String messagesBodyFolder = "/home/user/Dropbox/Rick/WebGatherer/Input/emails/jobApplication/body";
    private static final String attachmentFilePath = "/home/user/Dropbox/Rick/RickDane_Java_SoftwareDeveloper.doc";
    private static final String singleFileInputPath = "/home/user/Dropbox/Rick/WebGatherer/Output/emails/extractedEmails.txt";

    private static ReadFiles readFiles;

    private static List<String> domainsSent = new ArrayList<String>();

    private static final int delayBetweenEmails = 30000;
    private static boolean onlyDoFirstPage = true;

    private static final int minDelay = 90000;
    private static final int maxDelay = 260000;
    private static RandomSelector randomSelector;

    private static boolean blockMultiEmailsToSameDomain = false;

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new DependencyBindingModule());
        SendEmail sendEmail = injector.getInstance(SendEmail.class);

        randomSelector = injector.getInstance(RandomSelector.class);

        readFiles = injector.getInstance(ReadFiles.class);
        RandomSelector randomSelector = injector.getInstance(RandomSelector.class);
        EmailExtractor emailExtractor = injector.getInstance(EmailExtractor.class);

        List<String> emailSubjects = readFiles.readFilesToCollection(messagesSubjectFolder);
        List<String> emailBodies = readFiles.readFilesToCollection(messagesBodyFolder);

        //sendEmail.configure("Rick Dane", "smtp.gmx.com", "rick_developer@gmx.com", "blient8030", "465");
        sendEmail.configure("Rick Dane", "smtp.gmail.com", "r.dane1010@gmail.com", "blient8030", "465");

        Collection<String> emailAddresses = readFiles.readLinesToList(singleFileInputPath);

        for (String curEmail : emailAddresses) {
            System.out.println("iteration");

            String[] emailSplit = curEmail.split("@");

            if (blockMultiEmailsToSameDomain && domainsSent.contains(emailSplit[1])) {
                PersistenceImpl_WriteToFile.appendToFile(notSentFilePath, curEmail + "\n");
            } else {
                String body = emailBodies.get(randomSelector.randomListIndex(emailBodies));
                String subject = emailSubjects.get(randomSelector.randomListIndex(emailSubjects));
                sendEmail.sendEmail(body, subject, curEmail, attachmentFilePath);   //curEmail

                System.out.println("sending email to: " + curEmail);

                PersistenceImpl_WriteToFile.appendToFile(outputFilePath, curEmail + "\n");
                domainsSent.add(emailSplit[1]);
                try {
                    int delay = randomSelector.generateRandomNumberInRange(minDelay, maxDelay);
                    System.out.println("Sleeping for" + delay / 1000 + " seconds");

                    Thread.sleep(randomSelector.generateRandomNumberInRange(minDelay, maxDelay));
                    System.out.println("finished sleep");
                } catch (InterruptedException e) {
                }
            }
        }
    }

}
