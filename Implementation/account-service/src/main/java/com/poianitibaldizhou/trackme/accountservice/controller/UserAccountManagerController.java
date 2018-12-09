package com.poianitibaldizhou.trackme.accountservice.controller;

import com.poianitibaldizhou.trackme.accountservice.assembler.UserAssembler;
import com.poianitibaldizhou.trackme.accountservice.entity.User;
import com.poianitibaldizhou.trackme.accountservice.service.UserAccountManagerService;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Entry point for accessing the service that regards the account of users
 */
@RestController
@RequestMapping(path = "/useraccountservice")
public class UserAccountManagerController {

    private final UserAccountManagerService service;

    private final UserAssembler userAssembler;

    /**
     * Creates a new entry point for accessing the services that regard the accounts
     * of users
     *
     * @param userAccountManagerService service that manages the user accounts: needed
     *                                  in order to access the business function of the service
     * @param userAssembler assembler for user that adds hal content
     */
    UserAccountManagerController(UserAccountManagerService userAccountManagerService,
                                 UserAssembler userAssembler) {
        this.service = userAccountManagerService;
        this.userAssembler = userAssembler;
    }

    /**
     * This method will return a user identified by the parameter ssn specified in the path variable
     *
     * @param ssn ssn that identifies the requested user
     * @return resource containing the user and useful links
     */
    @GetMapping("/users/{ssn}")
    public @ResponseBody Resource<User> getUserBySsn(@PathVariable String ssn) {
        return userAssembler.toResource(service.getUserBySsn(ssn));
    }

    /**
     * Register a user into the system
     *
     * @param ssn ssn of the new user
     * @param user information related to the user who is trying to register
     * @return an http 201 created message that contains the newly formed link
     * @throws URISyntaxException due to the creation of a new URI resource
     */
    @PostMapping("/users/{ssn}")
    public @ResponseBody ResponseEntity<?> registerUser(@PathVariable String ssn, @RequestBody User user) throws URISyntaxException {
        user.setSsn(ssn);

        Resource<User> resource = userAssembler.toResource(service.registerUser(user));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }
}
