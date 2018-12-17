package com.trackme.trackmeapplication.baseUtility;

/**
 * Class whit all the constant string value in the project. It allows to a easily refactor of the
 * messages and a better maintainability of the code.
 *
 * @author Mattia Tibaldi
 *
 */
public final class Constant {

    public static final String SSN_PATTERN = "^\\d{3}-\\d{2}-\\d{4}$";
    public static final String E_MAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    public static final String DATE_PATTERN = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";

    public static final String FINISH_ACTION = "finish_activity";

    public static final String LOGIN_SHARED_DATA_NAME = "login_data";
    public static final String SD_USERNAME_DATA_KEY = "username";
    public static final String SD_EMAIL_DATA_KEY = "email";
    public static final String USER_LOGGED_BOOLEAN_VALUE_KEY = "user_logged";
    public static final String BUSINESS_LOGGED_BOOLEAN_VALUE_KEY = "business_logged";
}
