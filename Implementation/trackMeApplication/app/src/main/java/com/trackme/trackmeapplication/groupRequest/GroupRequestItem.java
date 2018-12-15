package com.trackme.trackmeapplication.groupRequest;

public class GroupRequestItem {

    private String thirdPartyName;
    private String creationDate;
    private String aggregator;
    private String requestType;
    private String status;
    private String filter;

    public GroupRequestItem(String thirdPartyName, String creationDate, String aggregator, String requestType, String filter) {
        this.thirdPartyName = thirdPartyName;
        this.creationDate = creationDate;
        this.aggregator = aggregator;
        this.requestType = requestType;
        this.filter = filter;
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public String getFilter() {
        return filter;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getAggregator() {
        return aggregator;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getStatus() {
        return status;
    }
}
