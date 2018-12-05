package com.poianitibaldizhou.trackme.sharedataservice.controller;

import com.poianitibaldizhou.trackme.sharedataservice.service.AccessDataService;
import com.poianitibaldizhou.trackme.sharedataservice.util.DataWrapper;
import org.springframework.web.bind.annotation.*;

/**
 * Entry point for accessing services regarding the access of data
 */
@RestController
@RequestMapping("/accessdata")
public class AccessDataController {

    private AccessDataService accessDataService;

    public AccessDataController(AccessDataService accessDataService) {
        this.accessDataService = accessDataService;
    }

    /**
     * Retrieves all the health and position data of the user defined inside the individual request
     *
     * @param thirdPartyId the id of the third party asking for data
     * @param individualRequestId the id of the individual request made by the third party on a certain user
     * @return a OK http response with all the data defined in the individual request
     */
    @GetMapping("/individualrequest/{third_party_id}/{request_id}")
    public @ResponseBody DataWrapper getIndividualRequestData(@PathVariable(name = "third_party_id") Long thirdPartyId,
                                                              @PathVariable(name = "request_id") Long individualRequestId){
        return accessDataService.getIndividualRequestData(thirdPartyId, individualRequestId);
    }

    /**
     * Retrieves the aggregated data requested by the third party {third_party_id} with the group request {request_id}
     *
     * @param thirdPartyId the id of the third party asking for data
     * @param groupRequestId the id of the group request regarding the data
     * @return a OK http response with the aggregated data requested
     */
    @GetMapping("/grouprequest/{third_party_id}/{request_id}")
    public @ResponseBody String getGroupRequestData(@PathVariable(name = "third_party_id") Long thirdPartyId ,
                                                    @PathVariable(name = "request_id") Long groupRequestId){
        return accessDataService.getGroupRequestData(thirdPartyId, groupRequestId).toString();
    }
}
