package com.rickdane.springmodularizedproject.api.transport;

/**
 * @author Rick Dane
 */
public class Scraper {

    public enum ProcessStatus {

        NOT_PROCESSED, PROCESSED, IN_PROGRESS

    }

    public enum Type {
        CRAIGSLIST, INDEED, EMAIL_SCRAPE, URL_SCRAPE;
    }

    private Type type;

    private String name;

    private int version;

    private Long id;

    private ProcessStatus status;

    private boolean isProcessed;

    private String urlPrefix;

    private String urlPostfix;

    private String keyword;

    private String baseDomainName;

    private int pageIncrementAmnt;

    public int getPageIncrementAmnt() {
        return pageIncrementAmnt;
    }

    public void setPageIncrementAmnt(int pageIncrementAmnt) {
        this.pageIncrementAmnt = pageIncrementAmnt;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getUrlPostfix() {
        return urlPostfix;
    }

    public void setUrlPostfix(String urlPostfix) {
        this.urlPostfix = urlPostfix;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getBaseDomainName() {
        return baseDomainName;
    }

    public void setBaseDomainName(String baseDomainName) {
        this.baseDomainName = baseDomainName;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
