package com.poianitibaldizhou.trackme.individualrequestservice.controller;

import com.poianitibaldizhou.trackme.individualrequestservice.assembler.IndividualRequestResourceAssembler;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.service.IndividualRequestManagerService;
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
@RequestMapping(path = "/individualrequestservice")
public class IndividualRequestController {

    private final IndividualRequestManagerService requestManagerService;

    private final IndividualRequestResourceAssembler assembler;

    /**
     * Creates a new entry point for accessing the service that regards the individual request
     *
     * @param individualRequestManagerService service that manages the individual request: needed in order to
     *                                        accessing the business functions of the service
     * @param assembler assembler for individual request that adds hypermedia content (HAL)
     */
    IndividualRequestController(IndividualRequestManagerService individualRequestManagerService, IndividualRequestResourceAssembler assembler) {
        this.requestManagerService = individualRequestManagerService;
        this.assembler = assembler;
    }

    // User and third party access point to the service

    /**
     * This method will return a request identified with a certain id, provided with some useful links
     *
     * @param id id of the demanded request
     * @return resource containing the individual request
     */
    @GetMapping("/requests/{id}")
    public @ResponseBody Resource<IndividualRequest> getRequestById(@PathVariable Long id) {
        return assembler.toResource(requestManagerService.getRequestById(id));
    }


    // User access point to the service

    /**
     * This method will return the requests of a certain user, that are marked with status PENDING
     *
     * @param ssn the set of pending regards the user specified with this ssn
     * @return set of resources of size 2: the first item is the set of pending requests, embedded with
     * their own link. The second one provides a self reference to this method.
     */
    @GetMapping("requests/users/{ssn}")
    public @ResponseBody Resources<Resource<IndividualRequest>> getUserPendingRequests(@PathVariable String ssn) {
        User user = new User(ssn);
        List<Resource<IndividualRequest>> pendingRequests = requestManagerService.getUserPendingRequests(user).stream()
                .map(assembler::toResource).collect(Collectors.toList());

        return new Resources<>(pendingRequests,
                linkTo(methodOn(IndividualRequestController.class).getUserPendingRequests(ssn)).withSelfRel());
    }

    // Third party customer access point to the service

    /**
     * This method will return the requests performed by a certain third party customer
     *
     * @param thirdPartyID the set of requests are performed by the third party customer identified with this number
     * @return set of resources of size 2: the first item is the set of demanded requests, embedded with their own
     * link. The second one provides a self reference to this method
     */
    @GetMapping("/requests/thirdparty/{thirdPartyID}")
    public @ResponseBody Resources<Resource<IndividualRequest>> getThirdPartyRequests(@PathVariable Long thirdPartyID) {
        List<Resource<IndividualRequest>> requests = requestManagerService.getThirdPartyRequests(thirdPartyID).stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(requests,
                linkTo(methodOn(IndividualRequestController.class).getThirdPartyRequests(thirdPartyID)).withSelfRel());
    }

    /**
     * Add a new request to the set of individual request.
     * The request will not be added in the case in which it is performed on a non existing user.
     *
     * @param ssn the request regards the user identified by this field
     * @param newRequest request that will be added to the system
     * @return an http 201 created message that contains the newly formed link
     * @throws URISyntaxException due to the creation of a new URI resource
     */
    @PostMapping("/requests/{ssn}")
    public @ResponseBody ResponseEntity<?> newRequest(@PathVariable String ssn, @RequestBody IndividualRequest newRequest) throws URISyntaxException {
        newRequest.setUser(new User(ssn));

        Resource<IndividualRequest> resource = assembler.toResource(requestManagerService.addRequest(newRequest));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }
}
