package com.poianitibaldizhou.trackme.apigateway.util;

/**
 * Constants used into the project
 */
public class Constants {
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

    public static final String SPACE = " ";
}
