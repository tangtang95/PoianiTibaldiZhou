package com.poianitibaldizhou.trackme.apigateway.controller;

import com.poianitibaldizhou.trackme.apigateway.assembler.UserAssembler;
import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.security.service.UserAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.UserAccountManagerService;
import com.poianitibaldizhou.trackme.apigateway.util.Constants;
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
@RequestMapping("/public/users")
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
    @PostMapping("/{ssn}")
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
    @PostMapping("/authenticate")
    @ResponseBody String login(@RequestParam("username") final String username,  @RequestParam("password") final String password) {
        return userAuthenticationService.userLogin(username, password).orElseThrow(() -> new BadCredentialsException(Constants.USER_BAD_CREDENTIAL));
    }
}
