package com.poianitibaldizhou.trackme.apigateway.controller;

import com.poianitibaldizhou.trackme.apigateway.assembler.ThirdPartyCompanyAssembler;
import com.poianitibaldizhou.trackme.apigateway.assembler.ThirdPartyPrivateAssembler;
import com.poianitibaldizhou.trackme.apigateway.service.ThirdPartyAccountManagerService;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyCompanyWrapper;
import com.poianitibaldizhou.trackme.apigateway.util.ThirdPartyPrivateWrapper;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * Entry point for accessing the service that regards the account of third party customers
 */
@RestController
@RequestMapping(path = "/tpaccountservice")
public class ThirdPartyCustomerManagerController {

    private final ThirdPartyAccountManagerService service;

    private final ThirdPartyCompanyAssembler thirdPartyCompanyAssembler;

    private final ThirdPartyPrivateAssembler thirdPartyPrivateAssembler;

    /**
     * Creates a new entry point for accessing the services that regard the accounts
     * of third party customers
     *
     * @param service             service that manages the third party custommers accounts: needed
     *                            in order to access the business function of the service
     * @param thirdPartyCompanyAssembler assembler for third party customer related with companies, that adds hal content
     * @param thirdPartyPrivateWrapper assembler for third party customer non-related with companies, that adds hal content
     */
    ThirdPartyCustomerManagerController(ThirdPartyAccountManagerService service,
                                               ThirdPartyCompanyAssembler thirdPartyCompanyAssembler,
                                               ThirdPartyPrivateAssembler thirdPartyPrivateWrapper) {
        this.service = service;
        this.thirdPartyCompanyAssembler = thirdPartyCompanyAssembler;
        this.thirdPartyPrivateAssembler = thirdPartyPrivateWrapper;
    }

    /**
     * This method will return information regarding a third party customer identified by a certain email
     *
     * @param email email of the requested third party customer
     * @return contains information regarding the third party customer and, either its company details or
     * its private detail (note that both is impossible)
     */
    @GetMapping("/thirdparties/{email}")
    public @ResponseBody
    Resource<Object> getThirdParty(@PathVariable String email) {
        Optional<ThirdPartyCompanyWrapper> thirdPartyCompanyWrapper = service.getThirdPartyCompanyByEmail(email);
        if(thirdPartyCompanyWrapper.isPresent()) {
            return new Resource<>(thirdPartyCompanyAssembler.toResource(thirdPartyCompanyWrapper.get()));
        }

        Optional<ThirdPartyPrivateWrapper> thirdPartyPrivateWrapper = service.getThirdPartyPrivateByEmail(email);
        if(thirdPartyPrivateWrapper.isPresent()) {
            return new Resource<>(thirdPartyPrivateAssembler.toResource(thirdPartyPrivateWrapper.get()));
        }

        throw new IllegalStateException();
    }

    /**
     * Register a third party customer as a company
     *
     * @param thirdPartyCompanyWrapper information regarding the customer and its company
     * @return an http 201 created message that contains the newly formed link
     * @throws URISyntaxException due to the creation of a new URI resource
     */
    @PostMapping("/thirdparties/companies")
    public @ResponseBody
    ResponseEntity<?> registerCompanyThirdParty(@RequestBody ThirdPartyCompanyWrapper thirdPartyCompanyWrapper)
            throws URISyntaxException {
        Resource<ThirdPartyCompanyWrapper> resource = thirdPartyCompanyAssembler.
                toResource(service.registerThirdPartyCompany(thirdPartyCompanyWrapper));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    /**
     * Register a third party customer as a non-company
     *
     * @param thirdPartyPrivateWrapper information regarding the customer and its private detail
     * @return an http 201 created message that contains the newly formed link
     * @throws URISyntaxException due to the creation of a new URI resource
     */
    @PostMapping("/thirdparties/privates")
    public @ResponseBody
    ResponseEntity<?> registerPrivateThirdParty(@RequestBody ThirdPartyPrivateWrapper thirdPartyPrivateWrapper)
            throws URISyntaxException {

        Resource<ThirdPartyPrivateWrapper> resource = thirdPartyPrivateAssembler.
                toResource(service.registerThirdPartyPrivate(thirdPartyPrivateWrapper));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }
}

