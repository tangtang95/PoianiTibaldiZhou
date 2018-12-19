package com.poianitibaldizhou.trackme.individualrequestservice.controller;

import com.poianitibaldizhou.trackme.individualrequestservice.assembler.IndividualRequestWrapperResourceAssembler;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.ThirdParty;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.exception.ImpossibleAccessException;
import com.poianitibaldizhou.trackme.individualrequestservice.service.IndividualRequestManagerService;
import com.poianitibaldizhou.trackme.individualrequestservice.util.Constants;
import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestWrapper;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Entry point for accessing the service that regards the individual request
 */
@RestController
@RequestMapping(path = Constants.REQUEST_API)
public class IndividualRequestController {

    private final IndividualRequestManagerService requestManagerService;

    private final IndividualRequestWrapperResourceAssembler assembler;

    /**
     * Creates a new entry point for accessing the service that regards the individual request
     *
     * @param individualRequestManagerService service that manages the individual request: needed in order to
     *                                        accessing the business functions of the service
     * @param assembler assembler for individual request that adds hypermedia content (HAL)
     */
    IndividualRequestController(IndividualRequestManagerService individualRequestManagerService, IndividualRequestWrapperResourceAssembler assembler) {
        this.requestManagerService = individualRequestManagerService;
        this.assembler = assembler;
    }

    // User and third party access point to the service

    /**
     * This method will return a request identified with a certain id, provided with some useful links
     *
     * @param requestingThirdParty third party customer that is accessing this method
     * @param requestingUser user that is accessing this method
     * @param id id of the demanded request
     * @return resource containing the individual request
     */
    @GetMapping(path = Constants.REQUEST_BY_ID_API)
    public @ResponseBody Resource<IndividualRequestWrapper> getRequestById(@RequestHeader(value = Constants.HEADER_THIRD_PARTY_ID) String requestingThirdParty,
                                                                    @RequestHeader(value = Constants.HEADER_USER_SSN) String requestingUser,
                                                                    @PathVariable Long id) {
        IndividualRequestWrapper request = IndividualRequestWrapper.convertIntoWrapper(requestManagerService.getRequestById(id));

        if(requestingUser.isEmpty() && requestingThirdParty.isEmpty())
            throw new ImpossibleAccessException();

        if(!requestingThirdParty.isEmpty() && request.getThirdPartyId() != Long.parseLong(requestingThirdParty)) {
                throw new ImpossibleAccessException();
        }

        if(!requestingUser.isEmpty() && !request.getUserSsn().equals(requestingUser))
                throw new ImpossibleAccessException();

        return assembler.toResource(IndividualRequestWrapper.convertIntoWrapper(requestManagerService.getRequestById(id)));
    }


    // User access point to the service

    /**
     * This method will return the requests of a certain user, that are marked with status PENDING
     *
     * @param requestingUser user that is accessing this method
     * @param ssn the set of pending regards the user specified with this ssn
     * @return set of resources of size 2: the first item is the set of pending requests, embedded with
     * their own link. The second one provides a self reference to this method.
     */
    @GetMapping(path = Constants.PENDING_REQUEST_BY_USER_API)
    public @ResponseBody Resources<Resource<IndividualRequestWrapper>> getUserPendingRequests(@RequestHeader(value = Constants.HEADER_USER_SSN) String requestingUser, @PathVariable String ssn) {
        if(!requestingUser.equals(ssn))
            throw new ImpossibleAccessException();

        User user = new User(ssn);
        List<Resource<IndividualRequestWrapper>> pendingRequests = requestManagerService.getUserPendingRequests(user).stream()
                .map(IndividualRequestWrapper::convertIntoWrapper)
                .map(assembler::toResource).collect(Collectors.toList());

        return new Resources<>(pendingRequests,
                linkTo(methodOn(IndividualRequestController.class).getUserPendingRequests(requestingUser, ssn)).withSelfRel());
    }

    // Third party customer access point to the service

    /**
     * This method will return the requests performed by a certain third party customer
     *
     * @param requestingThirdParty third party that requests the access to this method
     * @param thirdParty the set of requests are performed by the third party customer identified with this number
     * @return set of resources of size 2: the first item is the set of demanded requests, embedded with their own
     * link. The second one provides a self reference to this method
     */
    @GetMapping(path = Constants.REQUEST_BY_THIRD_PARTY_ID)
    public @ResponseBody Resources<Resource<IndividualRequestWrapper>> getThirdPartyRequests(@RequestHeader(value = Constants.HEADER_THIRD_PARTY_ID) String requestingThirdParty,
                                                                                      @PathVariable Long thirdParty) {
        if(Long.parseLong(requestingThirdParty) != thirdParty)
            throw new ImpossibleAccessException();

        List<Resource<IndividualRequestWrapper>> requests = requestManagerService.getThirdPartyRequests(thirdParty).stream()
                .map(IndividualRequestWrapper::convertIntoWrapper)
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(requests,
                linkTo(methodOn(IndividualRequestController.class).getThirdPartyRequests(requestingThirdParty, thirdParty)).withSelfRel());
    }

    /**
     * Add a new request to the set of individual request.
     * The request will not be added in the case in which it is performed on a non existing user.
     *
     * @param requestingThirdParty id of the third party that is requesting this method
     * @param ssn the request regards the user identified by this field
     * @param newRequest request that will be added to the system
     * @return an http 201 created message that contains the newly formed link
     * @throws URISyntaxException due to the creation of a new URI resource
     */
    @PostMapping(path = Constants.NEW_REQUEST_API)
    public @ResponseBody ResponseEntity<?> newRequest(@RequestHeader(value = Constants.HEADER_THIRD_PARTY_ID) String requestingThirdParty, @PathVariable String ssn, @RequestBody IndividualRequest newRequest) throws URISyntaxException {
        newRequest.setUser(new User(ssn));
        newRequest.setThirdParty(new ThirdParty(Long.parseLong(requestingThirdParty)));

        Resource<IndividualRequestWrapper> resource = assembler.toResource(IndividualRequestWrapper
                .convertIntoWrapper(requestManagerService.addRequest(newRequest)));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }
}
