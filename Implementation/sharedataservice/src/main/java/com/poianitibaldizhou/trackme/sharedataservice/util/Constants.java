package com.poianitibaldizhou.trackme.sharedataservice.util;

/**
 * List of constants regarding path and key of ResourceBundles
 */
public class Constants {

    private Constants(){}

    // RESOURCE BUNDLE PATHS
    public static final String RESOURCE_STRING_PATH = "messagebundle.ShareDataMessages";

    // MESSAGES KEYS
    public static final String USER_NOT_FOUND_EXCEPTION_KEY = "userNotFoundException";
    public static final String GROUP_REQUEST_NOT_FOUND_EXCEPTION_KEY = "groupRequestNotFoundException";
    public static final String INDIVIDUAL_REQUEST_NOT_FOUND_EXCEPTION_KEY = "individualRequestNotFoundException";
    public static final String IMPOSSIBLE_ACCESS_EXCEPTION_KEY ="impossibleAccessException";

    // EXCHANGE AND QUEUES NAMES
    public static final String USER_EXCHANGE_NAME = "trackme.user-exchange";
    public static final String USER_CREATED_SHARE_DATA_QUEUE_NAME = "trackme.user.created.share-data-queue";

    public static final String GROUP_REQUEST_EXCHANGE_NAME = "trackme.grouprequest-exchange";
    public static final String GROUP_REQUEST_CREATED_SHARE_DATA_QUEUE_NAME = "trackme.grouprequest.created.share-data-queue";
    public static final String GROUP_REQUEST_ACCEPTED_SHARE_DATA_QUEUE_NAME = "trackme.grouprequest.accepted.share-data-queue";

    public static final String INDIVIDUAL_REQUEST_EXCHANGE_NAME = "trackme.individualrequest-exchange";
    public static final String INDIVIDUAL_REQUEST_ACCEPTED_SHARE_DATA_QUEUE_NAME = "trackme.individualrequest.accepted.share-data-queue";

    public static final String NUMBER_OF_USER_INVOLVED_EXCHANGE_NAME = "trackme.number-of-user-involved-exchange";
    public static final String NUMBER_OF_USER_INVOLVED_GENERATED_GROUP_REQUEST_QUEUE_NAME = "trackme.double-data.generated.group-request-queue";

    // API PATH ACCESS DATA CONTROLLER
    public static final String ACCESS_DATA_API = "/dataretrieval";
    public static final String GET_INDIVIDUAL_REQUEST_DATA_API = "/individualrequests/{request_id}/thirdparties/{third_party_id}";
    public static final String GET_GROUP_REQUEST_DATA_API = "/grouprequests/{request_id}/thirdparties/{third_party_id}";
    public static final String GET_OWN_DATA_API = "/users/{user_id}";

    // API PATH SEND DATA CONTROLLER
    public static final String SEND_DATA_API = "/datacollection";
    public static final String SEND_HEALTH_DATA_API = "/healthdata/{userId}";
    public static final String SEND_POSITION_DATA_API = "/positiondata/{userId}";
    public static final String SEND_DATA_CLUSTER_API = "/clusterdata/{userId}";

    // API HEADER
    public static final String HEADER_USER_SSN = "userSsn";
    public static final String HEADER_THIRD_PARTY_ID = "thirdPartyId";

    // PATH VARIABLES NAME
    public static final String DATA_FROM = "from";
    public static final String DATA_TO = "to";
    public static final String USER_ID = "user_id";
    public static final String TP_ID = "third_party_id";
    public static final String REQUEST_ID = "request_id";

}
