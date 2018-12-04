package com.poianitibaldizhou.trackme.grouprequestservice.controller;

import com.poianitibaldizhou.trackme.grouprequestservice.assembler.GroupRequestWrapperAssembler;
import com.poianitibaldizhou.trackme.grouprequestservice.service.GroupRequestManagerService;
import com.poianitibaldizhou.trackme.grouprequestservice.util.GroupRequestWrapper;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Entry point for accessing the service that regards the group requests
 */
@RestController
@RequestMapping(path = "/grouprequestservice")
public class GroupRequestController {

    private final GroupRequestManagerService requestManagerService;

    private final GroupRequestWrapperAssembler groupRequestWrapperAssembler;

    /**
     * Creates a new entry point for accessing the service that regards the group requests
     *
     * @param requestManagerService service that manages the group requests: needed in order to
     *                                        accessing the business functions of the service
     * @param groupRequestWrapperAssembler assembler for group request wrapper that adds hypermedia content (HAL)
     */
    GroupRequestController(GroupRequestManagerService requestManagerService,
                           GroupRequestWrapperAssembler groupRequestWrapperAssembler) {
        this.requestManagerService = requestManagerService;
        this.groupRequestWrapperAssembler = groupRequestWrapperAssembler;
    }

    // GET METHODS

    /**
     * This method will return a group request, with its related filter statements, identified by a certain id.
     *
     * @param id id of the interested group request
     * @return resource containing the requested individual request
     */
    @GetMapping("{id}")
    public @ResponseBody Resource<GroupRequestWrapper> getRequest(@PathVariable Long id) {
        return groupRequestWrapperAssembler.toResource(requestManagerService.getById(id));
    }

    /**
     * This method will access all the group requests performed by a certain third party customer
     *
     * @param thirdPartyId the set of requests are performed by the third party customer identified with this number
     * @return  set of resources of size 2: the first item is the set of demanded requests, embedded with their own
     * link. The second one provides a self reference to this method
     */
    @GetMapping("/thirdparty/{thirdPartyId}")
    public @ResponseBody Resources<Resource<GroupRequestWrapper>> getRequestByThirdParty(@PathVariable Long thirdPartyId) {
        List<Resource<GroupRequestWrapper>> requests = requestManagerService.getByThirdPartyId(thirdPartyId).stream()
                .map(groupRequestWrapperAssembler::toResource).collect(Collectors.toList());

        return new Resources<>(requests,
                linkTo(methodOn(GroupRequestController.class).getRequestByThirdParty(thirdPartyId)).withSelfRel());
    }

    // POST METHODS

    // TODO: add new request method
}
