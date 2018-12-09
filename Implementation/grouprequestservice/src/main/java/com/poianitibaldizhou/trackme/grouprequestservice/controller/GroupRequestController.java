package com.poianitibaldizhou.trackme.grouprequestservice.controller;

import com.poianitibaldizhou.trackme.grouprequestservice.assembler.GroupRequestWrapperAssembler;
import com.poianitibaldizhou.trackme.grouprequestservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.grouprequestservice.service.GroupRequestManagerService;
import com.poianitibaldizhou.trackme.grouprequestservice.util.GroupRequestWrapper;
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
    @GetMapping("/requests/{id}")
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
    @GetMapping("/requests/thirdparties/{thirdPartyId}")
    public @ResponseBody Resources<Resource<GroupRequestWrapper>> getRequestByThirdParty(@PathVariable Long thirdPartyId) {
        List<GroupRequestWrapper> requestWrappers = requestManagerService.getByThirdPartyId(thirdPartyId);

        List<Resource<GroupRequestWrapper>> requests = requestWrappers.stream()
                .map(groupRequestWrapperAssembler::toResource).collect(Collectors.toList());

        return new Resources<>(requests,
                linkTo(methodOn(GroupRequestController.class).getRequestByThirdParty(thirdPartyId)).withSelfRel());
    }

    // POST METHODS

    /**
     * This method will create a new group request
     *
     * @param groupRequestWrapper group request that is asked to be added inside the system
     * @return an entity containing information about the created request, embedded with useful links to accessing
     * the new resource
     * @throws URISyntaxException due to the creation of a new uri resource: this will throw some exception if the syntax
     * is not well expressed
     */
    @PostMapping("/requests/thirdparties/{thirdPartyId}")
    public @ResponseBody ResponseEntity<?> newRequest(@PathVariable Long thirdPartyId, @RequestBody GroupRequestWrapper groupRequestWrapper) throws URISyntaxException {
        // TODO maybe add check that position and health timestamp are actually timestamp
        groupRequestWrapper.getGroupRequest().setThirdPartyId(thirdPartyId);
        groupRequestWrapper.getFilterStatementList().forEach(filterStatement -> filterStatement.setGroupRequest(groupRequestWrapper.getGroupRequest()));

        Resource<GroupRequestWrapper> resource = groupRequestWrapperAssembler.toResource(requestManagerService.addGroupRequest(groupRequestWrapper));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }
}
