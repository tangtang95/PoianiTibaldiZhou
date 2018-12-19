package com.poianitibaldizhou.trackme.apigateway.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.apigateway.assembler.UserAssembler;
import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.security.service.UserAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.UserAccountManagerService;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
import com.poianitibaldizhou.trackme.apigateway.util.Views;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Public controller regarding the users: methods in there can be accessed without authentication
 */
@RestController
@RequestMapping(Constants.PUBLIC_USER_API)
public class PublicUserController {

    private final UserAssembler userAssembler;
    private final UserAccountManagerService service;
    private final UserAuthenticationService userAuthenticationService;

    /**
     * Creates a new public rest controller for the user accounts
     *
     * @param userAssembler user assembler useful to assemble information regarding users
     * @param userAccountManagerService account manager service that exposes the business functionality and
     *                                  can access persistent data on the user
     * @param userAuthenticationService authentication service that performs operations regarding the authentication
     *                              of new users
     */
    public PublicUserController(UserAssembler userAssembler, UserAccountManagerService userAccountManagerService,
                                UserAuthenticationService userAuthenticationService) {
        this.userAssembler = userAssembler;
        this.service = userAccountManagerService;
        this.userAuthenticationService = userAuthenticationService;
    }

    /**
     * Register a user into the system
     *
     * @param ssn ssn of the new user
     * @param user information related to the user who is trying to register
     * @return an http 201 created message that contains the newly formed link
     * @throws URISyntaxException due to the creation of a new URI resource
     */
    @JsonView(Views.Secured.class)
    @PostMapping(Constants.REGISTER_USER_API)
    public @ResponseBody
    ResponseEntity<?> registerUser(@PathVariable String ssn, @RequestBody User user) throws URISyntaxException {
        user.setSsn(ssn);

        Resource<User> resource = userAssembler.toResource(service.registerUser(user));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    /**
     * Log in a user into the system
     *
     * @param username user name of the user
     * @param password password of the user
     * @return token associated with the user
     */
    @PostMapping(Constants.LOGIN_USER_API)
    @ResponseBody String login(
            @RequestParam(Constants.LOGIN_USER_USERNAME_PARAM) final String username,
            @RequestParam(Constants.LOGIN_USER_PW_PARAM) final String password) {
        return userAuthenticationService.userLogin(username, password).orElseThrow(() -> new BadCredentialsException(Constants.USER_BAD_CREDENTIAL));
    }
}
