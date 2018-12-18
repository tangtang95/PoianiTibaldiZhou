package com.trackme.trackmeapplication.Request.individualRequest;

import android.support.annotation.NonNull;

import com.trackme.trackmeapplication.Request.RequestStatus;

import java.io.Serializable;

public class RequestItem implements Serializable {

    private String thirdPartyName;
    private String startDate;
    private String endDate;
    private String motive;
    private String ssn;

    private RequestStatus status;
    private String id;
    private String creationDate;

    public RequestItem(String ssn, String thirdPartyName, String startDate, String endDate, String motive) {
        this.thirdPartyName = thirdPartyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.motive = motive;
        this.ssn = ssn;
    }

    /**
     * Constructor. This constructor is use to build the receiver message from the server
     *
     * @param status status of the request.
     */
    public RequestItem(@NonNull String requestID, @NonNull RequestStatus status, @NonNull String creationDate) {
        this.status = status;
        this.id = requestID;
        this.creationDate = creationDate;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public String getID() {
        return id;
    }

    public String getCreationDate() {
        return creationDate;
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

    public String getMotive() {
        return motive;
    }

    public String getSsn() {
        return ssn;
    }
}
