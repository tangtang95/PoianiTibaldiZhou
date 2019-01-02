package com.trackme.trackmeapplication.request.individualRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trackme.trackmeapplication.request.RequestStatus;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IndividualRequestWrapper implements Serializable {

    private String userSsn;
    private String status;
    private String timestamp;
    private String thirdPartyName;
    private String startDate;
    private String endDate;
    private String motivation;

    private String responseLink;

    public IndividualRequestWrapper(){}

    public void setResponseLink(String responseLink) {
        this.responseLink = responseLink;
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

    String extractResponseLink(){
        return responseLink;
    }

    String getUserSsn(){return userSsn;}

}
