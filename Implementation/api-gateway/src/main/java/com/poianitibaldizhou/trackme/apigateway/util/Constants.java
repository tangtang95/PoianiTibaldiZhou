package com.poianitibaldizhou.trackme.apigateway.util;

/**
 * Constants used into the project
 */
public class Constants {

    /**
     * Private constructor
     */
    private Constants() {}

    // Exception messages
    public static final String ALREADY_PRESENT_SSN = "User with the following ssn is already registered";
    public static final String ALREADY_PRESENT_USERNAME = "User with the following username is already registered";
    public static final String USER_NOT_FOUND = "User with the following username is not registered";
    public static final String ALREADY_PRESENT_EMAIL = "Third party with the following email is already registered";
    public static final String THIRD_PARTY_NOT_FOUND = "Third party with the following email is not registered";
    public static final String INVALID_OPERATION = "Error executing the operation requested. Please check that the" +
            "format of the data provided is correct.";
    public static final String USER_BAD_CREDENTIAL = "Invalid username or password";
    public static final String THIRD_PARTY_BAD_CREDENTIAL = "Invalid email or password";
    public static final String AUTHENTICATION_FILTER_MISSING_TOKEN = "Missing authentication token";
    public static final String LOGGED_USER_NOT_FOUND = "Cannot find user with the following token ";
    public static final String ACCESS_CONTROL_EXCEPTION_USER = "Users can't access api reserved to third party customers";
    public static final String ACCESS_CONTROL_EXCEPTION_TP = "Third parties can't access api reserved to users";
    public static final String API_NOT_FOUND_EXCEPTION = "Api not found";
    public static final String ERROR_IN_RESPONSE = "Error in parsing the response";

    // Utils
    public static final String SPACE = " ";

    // Headers
    public static final String EMPTY_HEADER = "";
    public static final String THIRD_PARTY_ID_HEADER_KEY = "thirdPartyId";
    public static final String USER_SSN_HEADER_KEY = "userSsn";

    public static final String PUBLIC_API = "/public/**";
    public static final String JSON_HREF_QUERY = "$..href";
    public static final String PORT = "${server.port}";
    public static final String HTTPS_PREFIX = "https://";
    public static final String PORT_SEPARATOR = ":";

    public static final String UTF8_CHAR_SET = "UTF-8";
}

