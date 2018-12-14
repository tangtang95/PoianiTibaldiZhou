package com.trackme.trackmeapplication.individualRequest;

import java.io.Serializable;

public class RequestItem implements Serializable {

    private String thirdPartyName;
    private String startDate;
    private String endDate;
    private String motive;
    private String ssn;

    public RequestItem(String ssn, String thirdPartyName, String startDate, String endDate, String motive) {
        this.thirdPartyName = thirdPartyName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.motive = motive;
        this.ssn = ssn;
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
