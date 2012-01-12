package com.rickdane.springmodularizedproject.api.transport;

/**
 * @author Rick Dane
 */
public class Rawscrapeddata {

    private String url;

    private String emailAddress;

    private String text;

    private Long fkScraperId;

    private Rawscrapeddatamigrationstatus rawscrapeddatamigrationstatus;

    public enum Rawscrapeddatamigrationstatus {

        NOT_MIGRATED, MIGRATED;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getFkScraperId() {
        return fkScraperId;
    }

    public void setFkScraperId(Long fkScraperId) {
        this.fkScraperId = fkScraperId;
    }

    public Rawscrapeddatamigrationstatus getRawscrapeddatamigrationstatus() {
        return rawscrapeddatamigrationstatus;
    }

    public void setRawscrapeddatamigrationstatus(Rawscrapeddatamigrationstatus rawscrapeddatamigrationstatus) {
        this.rawscrapeddatamigrationstatus = rawscrapeddatamigrationstatus;
    }
}
