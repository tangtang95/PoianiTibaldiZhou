package com.poianitibaldizhou.trackme.apigateway.controller;

import com.poianitibaldizhou.trackme.apigateway.assembler.UserAssembler;
import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.security.service.UserAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.UserAccountManagerService;
import org.springframework.hateoas.Resource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * Entry point for accessing the service that regards the account of users that are secured.
 * Indeed, authentication is needed in order to access the methods provided here.
 */
@RestController
@RequestMapping(path = "/users")
public class SecuredUserController {

    private final UserAccountManagerService service;

    private final UserAssembler userAssembler;

    private final UserAuthenticationService userAuthenticationService;

    /**
     * Creates a new entry point for accessing the services that regard the accounts
     * of users
     *
     * @param userAccountManagerService service that manages the user accounts: needed
     *                                  in order to access the business function of the service
     * @param userAssembler assembler for user that adds hal content
     * @param userAuthenticationService service that manages the authentication of users
     */
    SecuredUserController(UserAccountManagerService userAccountManagerService,
                          UserAssembler userAssembler,
                          UserAuthenticationService userAuthenticationService) {
        this.service = userAccountManagerService;
        this.userAssembler = userAssembler;
        this.userAuthenticationService = userAuthenticationService;
    }

    /**
     * This method will return information the specified user
     *
     * @param user user that will be returned
     * @return resource containing the user and useful links
     */
    @GetMapping("/info")
    public @ResponseBody Resource<User> getUser(@NotNull @AuthenticationPrincipal final User user) {
        System.out.println("HERE: " + user);
        return userAssembler.toResource(service.getUserBySsn(user.getSsn()));
    }

    /**
     * Logout the user that has called the method
     *
     * @param user user to logout
     * @return true
     */
    @GetMapping("/logout")
    public @ResponseBody boolean logout(@NotNull @AuthenticationPrincipal final User user) {
        userAuthenticationService.userLogout(user);
        return true;
    }
}
