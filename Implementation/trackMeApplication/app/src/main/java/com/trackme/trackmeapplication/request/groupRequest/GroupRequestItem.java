package com.trackme.trackmeapplication.request.groupRequest;

import android.support.annotation.NonNull;

import com.trackme.trackmeapplication.request.RequestStatus;

/**
 * Group request object.
 *
 * @author Mattia Tibaldi
 */
public class GroupRequestItem {

    /*private attributes*/
    private String thirdPartyName = null;
    private String creationDate;
    private String aggregator = null;
    private String requestType = null;
    private RequestStatus status = null;
    private String filter = null;
    private String id = null;

    /**
     * Constructor. The client use this constructor to build the group request message to sent to
     * server.
     *
     * @param thirdPartyName sender name
     * @param creationDate date of the creation
     * @param aggregator aggregator chosen
     * @param requestType request type chosen
     * @param filter filter set in the request
     */
    GroupRequestItem(String thirdPartyName, String creationDate, String aggregator, String requestType, String filter) {
        this.thirdPartyName = thirdPartyName;
        this.creationDate = creationDate;
        this.aggregator = aggregator;
        this.requestType = requestType;
        this.filter = filter;
    }

    /**
     * Constructor. This constructor is use to build the receiver message from the server
     *
     * @param status status of the request.
     */
    public GroupRequestItem(@NonNull String requestID, @NonNull RequestStatus status, @NonNull String creationDate) {
        this.status = status;
        this.id = requestID;
        this.creationDate = creationDate;
    }

    /*Getter method*/

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

    public String getID() { return id; }

    public String getRequestType() {
        return requestType;
    }

    public RequestStatus getStatus() {
        return status;
    }
}
