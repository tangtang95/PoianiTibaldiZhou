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


}
