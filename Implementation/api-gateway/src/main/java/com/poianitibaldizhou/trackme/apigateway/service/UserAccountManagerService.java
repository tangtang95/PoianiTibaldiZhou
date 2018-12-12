package com.poianitibaldizhou.trackme.apigateway.service;

import com.poianitibaldizhou.trackme.apigateway.entity.User;

/**
 * Interfaces provided to offer the services that manage the accounts of users
 */
public interface UserAccountManagerService {

    /**
     * Retrieves an user based on a ssn, which is the id of the entity.
     * This requires, to be successful, that an user with a certain ssn exists
     *
     * @param ssn ssn that identifies the requested user
     * @return user identified by the ssn
     */
    User getUserBySsn(String ssn);

    /**
     * Adds an user to the system: a person is allowed to register into the system as a user by providing
     * a user-name, a password, his credentials, his social security number and
     * The required credentials are the following: first name, last name, birth date, birth city and birth nation
     *
     * @return the registered user into the system
     */
    User registerUser(User user);

    /**
     * Verify that the parameters provided math a user who has registered to the system
     *
     * @param username username of the user
     * @param password password for the user identified by the username
     * @return true if the credential are matched with a registered user, false otherwise
     */
    boolean verifyUserCredential(String username, String password);
}
