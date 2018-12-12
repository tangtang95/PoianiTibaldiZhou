package com.poianitibaldizhou.trackme.apigateway.service;

import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentSsnException;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentUsernameException;
import com.poianitibaldizhou.trackme.apigateway.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.apigateway.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the services regarding the management user accounts
 */
@Service
public class UserAccountManagerServiceImpl implements UserAccountManagerService {

    private final UserRepository userRepository;

    /**
     * Creates the manager of the services regarding the account of the users.
     * It needs a repository in order to make some operations on data (e.g. register a user)
     *
     * @param userRepository repository regarding the users
     */
    public UserAccountManagerServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User getUserBySsn(String ssn) {
        return userRepository.findById(ssn).orElseThrow(() -> new UserNotFoundException(ssn));
    }

    @Transactional
    @Override
    public User registerUser(User user) {
        if(userRepository.findById(user.getSsn()).isPresent()) {
            throw new AlreadyPresentSsnException(user.getSsn());
        }
        if(userRepository.findByUserName(user.getUserName()).isPresent()) {
            throw new AlreadyPresentUsernameException(user.getUserName());
        }

        return userRepository.saveAndFlush(user);
    }

    @Transactional
    @Override
    public boolean verifyUserCredential(String username, String password) {
        // TODO FIX EXCEPTION
        User user = userRepository.findByUserName(username).orElseThrow(() -> new UserNotFoundException(username));
        return user.getPassword().equals(password);
    }
}
