package com.trackme.trackmeapplication.request.individualRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trackme.trackmeapplication.request.RequestStatus;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestItem implements Serializable {

    private String ssn;
    private String email;

    private String status;
    private String timestamp;
    private String thirdPartyName;
    private String startDate;
    private String endDate;
    private String motivation;
    private String responseLink;

    public RequestItem(){}

    RequestItem(String ssn, String thirdPartyName, String startDate, String endDate, String motivation) {
        this.thirdPartyName = thirdPartyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.motivation = motivation;
        this.ssn = ssn;
    }

    public RequestStatus getStatus() {
        return RequestStatus.valueOf(status);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getMotivation() {
        return motivation;
    }

    public String getSsn() {
        return ssn;
    }

    public void setResponseLink(String responseLink) {
        this.responseLink = responseLink;
    }

    String extractResponseLink(){
        return responseLink;
    }

    String extractEmail() {
        return email;
    }
}
