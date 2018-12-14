package com.poianitibaldizhou.trackme.apigateway.service;

import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentSsnException;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentUsernameException;
import com.poianitibaldizhou.trackme.apigateway.exception.SsnNotFoundException;
import com.poianitibaldizhou.trackme.apigateway.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        return userRepository.findById(ssn).orElseThrow(() -> new SsnNotFoundException(ssn));
    }

    @Override
    public User getUserByUserName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    @Override
    public User registerUser(User user) {
        if(userRepository.findById(user.getSsn()).isPresent()) {
            throw new AlreadyPresentSsnException(user.getSsn());
        }
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new AlreadyPresentUsernameException(user.getUsername());
        }

        return userRepository.saveAndFlush(user);
    }

}
