package org.Webgatherer.ExperimentalLabs.Mail;

import com.rickdane.springmodularizedproject.api.transport.ReceivedEmail;
import com.sun.mail.pop3.POP3SSLStore;
import de.agitos.dkim.DKIMSigner;
import de.agitos.dkim.SMTPDKIMMessage;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.search.FlagTerm;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.Security;
import java.util.*;

/**
 * @author Rick Dane
 */
public class EmailSendReceive {

    private String smtpHost;
    private String fromName;
    private String userId;
    private String password;
    private String port;

    private String imap_address;
    private String imap_username;
    private String imap_password;

    public void configure(String fromName, String smtpHost, String userId, String password, String port) {
        this.fromName = fromName;
        this.smtpHost = smtpHost;
        this.userId = userId;
        this.password = password;
        this.port = port;
    }

    public void configureImap(String imap_address, String imap_username, String imap_password) {
        this.imap_address = imap_address;
        this.imap_username = imap_username;
        this.imap_password = imap_password;
    }

    public void sendEmail(String text, String subject, String to, String attachmentPath) {
        try {
            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpHost);
            props.setProperty("mail.transport.protocol", "smtps");
            props.put("mail.smtp.user", userId);
            props.put("mail.smtp.password", password);
            props.put("mail.smtp.port", port);
            props.put("mail.smtps.auth", "true");
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage message = new MimeMessage(session);
            InternetAddress fromAddress = null;
            InternetAddress toAddress = null;

            try {
                fromAddress = new InternetAddress(userId, fromName);
                toAddress = new InternetAddress(to);
            } catch (AddressException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            message.setFrom(fromAddress);
            message.setRecipient(Message.RecipientType.TO, toAddress);
            message.setSubject(subject);

            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(text);

            // create the second message part
            MimeBodyPart mbp2 = new MimeBodyPart();

            // attach the file to the message
            FileDataSource fds = new FileDataSource(attachmentPath);
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(fds.getName());

            // create the Multipart and add its parts to it
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);

            // add the Multipart to the message
            message.setContent(mp);

            // set the Date: header
            message.setSentDate(new Date());

            Transport transport = session.getTransport("smtps");
            transport.connect(smtpHost, userId, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public List<ReceivedEmail> retrieveUnreadEmails() {

        List<ReceivedEmail> receivedEmailList = new ArrayList<ReceivedEmail>();

        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        Store store = null;
        try {
            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect(imap_address, imap_username, imap_password);

            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);

            FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            Message messages[] = inbox.search(ft);

            for (Message message : messages) {

                ReceivedEmail receivedEmail = ReceivedEmail.createFromMessage(message);
                receivedEmailList.add(receivedEmail);
            }

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.exit(2);
        } finally {
            try {
                store.close();
            } catch (MessagingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return receivedEmailList;
    }

}
