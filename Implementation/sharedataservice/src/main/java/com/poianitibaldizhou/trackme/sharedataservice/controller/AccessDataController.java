package com.poianitibaldizhou.trackme.sharedataservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.sharedataservice.assembler.AggregatedDataResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.exception.ImpossibleAccessException;
import com.poianitibaldizhou.trackme.sharedataservice.service.AccessDataService;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatedData;
import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;
import com.poianitibaldizhou.trackme.sharedataservice.util.Views;
import org.springframework.hateoas.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Entry point for accessing services regarding the access of data
 */
@RestController
@RequestMapping("/dataretrieval")
public class AccessDataController {

    private AccessDataService accessDataService;

    private AggregatedDataResourceAssembler aggregatedDataResourceAssembler;

    public AccessDataController(AccessDataService accessDataService,
                                AggregatedDataResourceAssembler aggregatedDataResourceAssembler) {
        this.accessDataService = accessDataService;
        this.aggregatedDataResourceAssembler = aggregatedDataResourceAssembler;
    }

    /**
     * Retrieves all the health and position data of the user defined inside the individual request
     *
     * @param requestingThirdPartyId id of the third party customer that is trying to access this method
     * @param thirdPartyId the id of the third party asking for data
     * @param individualRequestId the id of the individual request made by the third party on a certain user
     * @return a OK http response with all the data defined in the individual request
     */
    @JsonView(Views.Public.class)
    @GetMapping("/individualrequests/{request_id}/thirdparties/{third_party_id}/")
    public @ResponseBody Resource<DataWrapper> getIndividualRequestData(@RequestHeader(value = "thirdPartyId") String requestingThirdPartyId,
                                                                        @PathVariable(name = "third_party_id") Long thirdPartyId,
                                                                        @PathVariable(name = "request_id") Long individualRequestId){
        if(Long.parseLong(requestingThirdPartyId) != thirdPartyId)
            throw new ImpossibleAccessException();

        return new Resource<>(accessDataService.getIndividualRequestData(thirdPartyId, individualRequestId),
                linkTo(methodOn(AccessDataController.class).getIndividualRequestData(requestingThirdPartyId
                        ,thirdPartyId, individualRequestId)).withSelfRel());
    }

    /**
     * Retrieves the aggregated data requested by the third party {third_party_id} with the group request {request_id}
     *
     * @param requestingThirdPartyId id of the third party customer that is trying to access this method
     * @param thirdPartyId the id of the third party asking for data
     * @param groupRequestId the id of the group request regarding the data
     * @return a OK http response with the aggregated data requested
     */
    @JsonView(Views.Public.class)
    @GetMapping("/grouprequests/{request_id}/thirdparties{third_party_id}/")
    public @ResponseBody Resource<AggregatedData> getGroupRequestData(@RequestHeader(value="thirdPartyId") String requestingThirdPartyId,
                                                                      @PathVariable(name = "third_party_id") Long thirdPartyId ,
                                                                      @PathVariable(name = "request_id") Long groupRequestId){
        if(Long.parseLong(requestingThirdPartyId) != thirdPartyId)
            throw new ImpossibleAccessException();

        return aggregatedDataResourceAssembler.toResource(accessDataService.getGroupRequestData(thirdPartyId, groupRequestId));
    }

    /**
     * Retrieves all the health and position data of the user {user_id} between two dates: from and to
     * @param requestingUser ssn of the user that is trying to access this method
     * @param userId the id of the user to retrieve data from
     * @param from the lower bound of data
     * @param to the upper bound of data
     * @return a OK http response with all the data of the user between the two dates
     */
    @JsonView(Views.Public.class)
    @GetMapping("/users/{user_id}")
    public @ResponseBody Resource<DataWrapper> getOwnData(@RequestHeader(value = "userSsn") String requestingUser,
                                                          @PathVariable(name = "user_id") String userId,
                                                          @RequestParam(name = "from") Date from,
                                                          @RequestParam(name = "to") Date to){
        if(!requestingUser.equals(userId))
            throw new ImpossibleAccessException();

        return new Resource<>(accessDataService.getOwnData(userId, from, to),
                linkTo(methodOn(AccessDataController.class).getOwnData(requestingUser, userId, from, to)).withSelfRel());

    }
}
