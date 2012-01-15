package com.rickdane.springmodularizedproject.api.transport;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 1/15/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReceivedEmail {

    private String fromAddress;

    private String toAddress;

    private String subject;

    private String content;

    public static ReceivedEmail createFromMessage(Message message) {

        ReceivedEmail receivedEmail = new ReceivedEmail();

        String contentTypeToRetrieve = "TEXT/PLAIN";

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

            receivedEmail.setFromAddress(fromArray[0].toString());
            receivedEmail.setToAddress(toArray[0].toString());
            receivedEmail.setSubject(message.getSubject().toString());

            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return receivedEmail;
    }


    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
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
