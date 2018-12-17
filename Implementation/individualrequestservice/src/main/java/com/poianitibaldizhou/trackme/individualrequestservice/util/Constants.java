package com.poianitibaldizhou.trackme.individualrequestservice.util;

/**
 * Constants string extrapolated in this class
 */
public class Constants {

    /**
     * Private constructor for hiding the public one
     */
    private Constants(){

    }

    // Constants that regards exception messages
    public static final String INVALID_CREDENTIAL = "Could not access this data";
    public static final String THIRD_PARTY_NOT_FOUND = "Could not find third party";
    public static final String REQUEST_ID_NOT_FOUND = "Could not find request";
    public static final String USER_NOT_FOUND = "Could not find user";
    public static final String RESPONSE_ALREADY_PRESENT = "Response already present for the request";
    public static final String NON_MATCHING_USER = "The following ssn is not the one related with the request:";
    public static final String TP_ALREADY_BLOCKED = "Third party customer already blocked";
    public static final String BAD_RESPONSE_TYPE = "Could not find response type";
    public static final String REFUSED_REQUEST_NOT_FOUND = "No refused request has been found of the following" +
            "third party customer";
    public static final String INCOMPATIBLE_DATE = "Start date must be before the end date";
    public static final String INVALID_OPERATION = "Error executing the operation requested. Please check that the" +
            "format of the data provided is correct.";

    public static final String SPACE = " ";

    // EXCHANGE AND QUEUES NAMES
    public static final String USER_EXCHANGE_NAME = "trackme.user-exchange";
    public static final String USER_CREATED_INDIVIDUAL_REQUEST_QUEUE_NAME = "trackme.user.created.individual-request-queue";

    public static final String INDIVIDUAL_REQUEST_EXCHANGE_NAME = "trackme.individualrequest-exchange";
    public static final String INDIVIDUAL_REQUEST_ACCEPTED_SHARE_DATA_QUEUE_NAME = "trackme.individualrequest.accepted.share-data-queue";

}
