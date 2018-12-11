package com.poianitibaldizhou.trackme.accountservice.service;

import com.poianitibaldizhou.trackme.accountservice.entity.User;
import com.poianitibaldizhou.trackme.accountservice.exception.AlreadyPresentSsnException;
import com.poianitibaldizhou.trackme.accountservice.exception.AlreadyPresentUsernameException;
import com.poianitibaldizhou.trackme.accountservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.accountservice.repository.UserRepository;
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
        // TODO
        return false;
    }
}
