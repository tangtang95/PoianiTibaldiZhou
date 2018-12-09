package com.poianitibaldizhou.trackme.accountservice.repository;

import com.poianitibaldizhou.trackme.accountservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for the users.
 * This is a jpa repository that accesses the persistent data regarding the users
 */
public interface UserRepository extends JpaRepository<User, String>{

    /**
     * Returns a user, if present, that matches with a certain username
     *
     * @param username username of the requested user
     * @return user that matches username
     */
    Optional<User> findByUserName(String username);
}