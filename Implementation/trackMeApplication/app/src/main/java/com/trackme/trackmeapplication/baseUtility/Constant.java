package com.trackme.trackmeapplication.baseUtility;

/**
 * Class whit all the constant string value in the project. It allows to a easily refactor of the
 * messages and a better maintainability of the code.
 *
 * @author Mattia Tibaldi
 *
 */
public final class Constant {

    /*String patterns*/
    public static final String SSN_PATTERN = "^\\d{3}-\\d{2}-\\d{4}$";
    public static final String E_MAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    public static final String DATE_PATTERN = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";

    public static final String REQUEST_FOLDER_NAME = "Request files";


    public static final String FINISH_ACTION = "finish_activity";

    /*String used in shared preference*/
    public static final String LOGIN_SHARED_DATA_NAME = "login_data";
    public static final String SD_USERNAME_DATA_KEY = "username";
    public static final String SD_EMAIL_DATA_KEY = "email";
    public static final String SD_TOKEN_KEY = "token";
    public static final String SD_INDIVIDUAL_REQUEST_KEY = "individual_request_item";
    public static final String USER_LOGGED_BOOLEAN_VALUE_KEY = "user_logged";
    public static final String BUSINESS_LOGGED_BOOLEAN_VALUE_KEY = "business_logged";

    // Api regarding third parties accounts
    public static final String PUBLIC_TP_API = "/public/thirdparties";
    public static final String REGISTER_COMPANY_TP_API = "/companies";
    public static final String REGISTER_PRIVATE_TP_API = "/privates";
    public static final String LOGIN_TP_API = "/authenticate";
    public static final String LOGIN_TP_EMAIL_API_PARAM = "email";
    public static final String LOGIN_TP_PW_API_PARAM = "password";
    public static final String SECURED_TP_API = "/thirdparties";
    public static final String GET_TP_INFO_API = "/info";
    public static final String LOGOUT_TP_API = "/logout";

    // Api regarding user accounts
    public static final String PUBLIC_USER_API = "/public/users";
    public static final String REGISTER_USER_API = "/{ssn}";
    public static final String LOGIN_USER_API = "/authenticate";
    public static final String LOGIN_USER_USERNAME_PARAM = "username";
    public static final String LOGIN_USER_PW_PARAM = "password";
    public static final String SECURED_USER_API = "/users";
    public static final String LOGOUT_USER_API = "/logout";
    public static final String GET_USER_INFO_API = "/info";

}
