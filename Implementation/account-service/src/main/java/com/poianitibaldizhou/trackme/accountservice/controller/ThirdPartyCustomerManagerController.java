package com.poianitibaldizhou.trackme.accountservice.controller;

import com.poianitibaldizhou.trackme.accountservice.assembler.ThirdPartyAssembler;
import com.poianitibaldizhou.trackme.accountservice.service.ThirdPartyAccountManagerService;
import com.poianitibaldizhou.trackme.accountservice.util.ThirdPartyCompanyWrapper;
import com.poianitibaldizhou.trackme.accountservice.util.ThirdPartyPrivateWrapper;
import com.poianitibaldizhou.trackme.accountservice.util.ThirdPartyWrapper;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Entry point for accessing the service that regards the account of third party customers
 */
@RestController
@RequestMapping(path = "/tpaccountservice")
public class ThirdPartyCustomerManagerController {

    private final ThirdPartyAccountManagerService service;

    private final ThirdPartyAssembler thirdPartyAssembler;

    /**
     * Creates a new entry point for accessing the services that regard the accounts
     * of third party customers
     *
     * @param service             service that manages the third party custommers accounts: needed
     *                            in order to access the business function of the service
     * @param thirdPartyAssembler assembler for third party customer that adds hal content
     */
    public ThirdPartyCustomerManagerController(ThirdPartyAccountManagerService service,
                                               ThirdPartyAssembler thirdPartyAssembler) {
        this.service = service;
        this.thirdPartyAssembler = thirdPartyAssembler;
    }

    /**
     * This method will return information regarding a third party customer identified by a certain email
     *
     * @param email email of the requested third party customer
     * @return contains information regarding the third party customer and, either its company details or
     * its private detail (note that both is impossible)
     */
    @GetMapping("/thirdparties")
    public @ResponseBody
    Resource<ThirdPartyWrapper> getThirdParty(@PathVariable String email) {
        return thirdPartyAssembler.toResource(service.getThirdPartyByEmail(email));
    }

    /**
     * Register a third party customer as a company
     *
     * @param email email of the third party customer
     * @param thirdPartyCompanyWrapper information regarding the customer and its company
     * @return an http 201 created message that contains the newly formed link
     * @throws URISyntaxException due to the creation of a new URI resource
     */
    @PostMapping("/thirdparties/companies")
    public @ResponseBody
    ResponseEntity<?> registerCompanyThirdParty(@PathVariable String email,
                                                @RequestBody ThirdPartyCompanyWrapper thirdPartyCompanyWrapper)
            throws URISyntaxException {

        thirdPartyCompanyWrapper.getThirdPartyCustomer().setEmail(email);

        Resource<ThirdPartyWrapper> resource = thirdPartyAssembler.toResource(service.registerThirdPartyCompany(thirdPartyCompanyWrapper));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    /**
     * Register a third party customer as a non-company
     *
     * @param email email of the third party customer
     * @param thirdPartyPrivateWrapper information regarding the customer and its private detail
     * @return an http 201 created message that contains the newly formed link
     * @throws URISyntaxException due to the creation of a new URI resource
     */
    @PostMapping("/thirdparties/private")
    public @ResponseBody
    ResponseEntity<?> registerPrivateThirdParty(@PathVariable String email,
                                                @RequestBody ThirdPartyPrivateWrapper thirdPartyPrivateWrapper)
            throws URISyntaxException {

        thirdPartyPrivateWrapper.getThirdPartyCustomer().setEmail(email);

        Resource<ThirdPartyWrapper> resource = thirdPartyAssembler.toResource(service.registerThirdPartyPrivate(thirdPartyPrivateWrapper));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }
}

