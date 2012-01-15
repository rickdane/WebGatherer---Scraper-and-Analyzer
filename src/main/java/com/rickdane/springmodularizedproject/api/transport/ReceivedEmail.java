package com.rickdane.springmodularizedproject.api.transport;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 1/15/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReceivedEmail {

    private String from;

    private String to;

    private String subject;

    private String content;

    public static ReceivedEmail createFromMessage(Message message) {

        ReceivedEmail receivedEmail = new ReceivedEmail();

        String contentTypeToRetrieve = "TEXT/HTML";

        try {

            Multipart mp = (Multipart) message.getContent();

            int mpCnt = mp.getCount() - 1;

            for (int i = 0; i <= mpCnt; i++) {
                BodyPart bodyPart = mp.getBodyPart(i);
                String contentType = bodyPart.getContentType();

                if (contentType.contains(contentTypeToRetrieve)) {
                    Object content = bodyPart.getContent();

                    receivedEmail.setContent(content.toString());
                    break;
                }
            }

            Address[] fromArray =  message.getFrom();
            Address[] toArray =  message.getReplyTo();

            receivedEmail.setFrom(fromArray[0].toString());
            receivedEmail.setTo(toArray[0].toString());
            receivedEmail.setSubject(message.getSubject().toString());

            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return receivedEmail;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
