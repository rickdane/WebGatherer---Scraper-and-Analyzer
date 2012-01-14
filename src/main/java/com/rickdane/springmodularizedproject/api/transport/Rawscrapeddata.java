package com.rickdane.springmodularizedproject.api.transport;

/**
 * @author Rick Dane
 */
public class Rawscrapeddata {

    private Long id;

    private int version;

    private String url;

    private String emailAddress;

    private String text;

    private Long fkScraperId;

    private Rawscrapeddatamigrationstatus rawscrapeddatamigrationstatus;

    private RawscrapeddataEmailScrapeAttempted rawscrapeddataEmailScrapeAttempted;

    public enum Rawscrapeddatamigrationstatus {

        NOT_MIGRATED, MIGRATED;
    }

    public enum RawscrapeddataEmailScrapeAttempted {
        ATTEMPTED, NOT_ATTEMPTED,IN_PROGRESS
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RawscrapeddataEmailScrapeAttempted getRawscrapeddataEmailScrapeAttempted() {
        return rawscrapeddataEmailScrapeAttempted;
    }

    public void setRawscrapeddataEmailScrapeAttempted(RawscrapeddataEmailScrapeAttempted rawscrapeddataEmailScrapeAttempted) {
        this.rawscrapeddataEmailScrapeAttempted = rawscrapeddataEmailScrapeAttempted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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
