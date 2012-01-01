package org.Webgatherer.ExperimentalLabs.Mail;

import de.agitos.dkim.DKIMSigner;
import de.agitos.dkim.SMTPDKIMMessage;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.util.*;

/**
 * @author Rick Dane
 */
public class SendEmail {

    private String smtpHost;
    private String fromName;
    private String userId;
    private String password;
    String port;

    public void configure(String fromName, String smtpHost, String userId, String password, String port) {
        this.fromName = fromName;
        this.smtpHost = smtpHost;
        this.userId = userId;
        this.password = password;
        this.port = port;
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
}

