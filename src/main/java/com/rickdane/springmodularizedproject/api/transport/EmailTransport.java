package com.rickdane.springmodularizedproject.api.transport;

/**
 * @author Rick Dane
 */
public class EmailTransport {

    private String toEmail;

    private String body;

    private String fromAddress;

    private String subject;

    private long fkEmailaddressID;

    public long getFkEmailaddressID() {
        return fkEmailaddressID;
    }

    public void setFkEmailaddressID(long fkEmailaddressID) {
        this.fkEmailaddressID = fkEmailaddressID;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
