package com.trackme.trackmeapplication.request.individualRequest;

import java.io.Serializable;

public class IndividualRequest implements Serializable {

    private String startDate;
    private String endDate;
    private String motivation;

    IndividualRequest(String startDate, String endDate, String motivation) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.motivation = motivation;
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
}
