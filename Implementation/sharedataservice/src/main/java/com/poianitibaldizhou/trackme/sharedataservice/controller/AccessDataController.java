package com.poianitibaldizhou.trackme.sharedataservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.sharedataservice.assembler.AggregatedDataResourceAssembler;
import com.poianitibaldizhou.trackme.sharedataservice.service.AccessDataService;
import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatedData;
import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;
import com.poianitibaldizhou.trackme.sharedataservice.util.Views;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Entry point for accessing services regarding the access of data
 */
@RestController
@RequestMapping("/accessdata")
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
     * @param thirdPartyId the id of the third party asking for data
     * @param individualRequestId the id of the individual request made by the third party on a certain user
     * @return a OK http response with all the data defined in the individual request
     */
    @JsonView(Views.Public.class)
    @GetMapping("/individualrequest/{third_party_id}/{request_id}")
    public @ResponseBody Resource<DataWrapper> getIndividualRequestData(@PathVariable(name = "third_party_id") Long thirdPartyId,
                                                              @PathVariable(name = "request_id") Long individualRequestId){
        return new Resource<>(accessDataService.getIndividualRequestData(thirdPartyId, individualRequestId),
                linkTo(methodOn(AccessDataController.class).getIndividualRequestData(thirdPartyId, individualRequestId)).withSelfRel());
    }

    /**
     * Retrieves the aggregated data requested by the third party {third_party_id} with the group request {request_id}
     *
     * @param thirdPartyId the id of the third party asking for data
     * @param groupRequestId the id of the group request regarding the data
     * @return a OK http response with the aggregated data requested
     */
    @JsonView(Views.Public.class)
    @GetMapping("/grouprequest/{third_party_id}/{request_id}")
    public @ResponseBody Resource<AggregatedData> getGroupRequestData(@PathVariable(name = "third_party_id") Long thirdPartyId ,
                                                 @PathVariable(name = "request_id") Long groupRequestId){
        return aggregatedDataResourceAssembler.toResource(accessDataService.getGroupRequestData(thirdPartyId, groupRequestId));
    }
}
