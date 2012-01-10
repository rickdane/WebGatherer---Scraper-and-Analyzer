package com.rickdane.springmodularizedproject.api.transport;

/**
 * @author Rick Dane
 */
public class WebGathererJobJsonTransport extends TransportBase {

    private String jobType;

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
}
