package com.poianitibaldizhou.trackme.grouprequestservice.util;

public class Constants {

    private Constants(){}

    // Exception messages
    public static final String REQUEST_ID_NOT_FOUND = "Group request not found";
    public static final String BAD_OPERATOR_REQUEST_TYPE = "Can't match the aggregator operator with that request type";
    public static final String INVALID_OPERATION = "Invalid operation. Please, check the format of the data that have been sent";
    public static final String IMPOSSIBLE_ACCESS = "Can't access this method";


    public static final String SPACE = " ";

    // EXCHANGE AND QUEUES NAMES
    public static final String GROUP_REQUEST_EXCHANGE_NAME = "trackme.grouprequest-exchange";
    public static final String GROUP_REQUEST_CREATED_SHARE_DATA_QUEUE_NAME = "trackme.grouprequest.created.share-data-queue";
    public static final String GROUP_REQUEST_ACCEPTED_SHARE_DATA_QUEUE_NAME = "trackme.grouprequest.accepted.share-data-queue";

    public static final String NUMBER_OF_USER_INVOLVED_EXCHANGE_NAME = "trackme.number-of-user-involved-exchange";
    public static final String NUMBER_OF_USER_INVOLVED_GENERATED_GROUP_REQUEST_QUEUE_NAME = "trackme.double-data.generated.group-request-queue";
}