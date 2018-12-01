package com.poianitibaldizhou.trackme.individualrequestservice.controller;

import com.poianitibaldizhou.trackme.individualrequestservice.assembler.BlockedThirdPartyResourceAssembler;
import com.poianitibaldizhou.trackme.individualrequestservice.assembler.ResponseResourceAssembler;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdParty;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.Response;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.User;
import com.poianitibaldizhou.trackme.individualrequestservice.service.UploadResponseService;
import com.poianitibaldizhou.trackme.individualrequestservice.util.ResponseType;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Entry point for accessing the service that regards the responses to individual request
 */
@RestController
@RequestMapping(path="/individualresponseservice")
public class ResponseController {

    private final UploadResponseService uploadResponseService;
    private final ResponseResourceAssembler responseAssembler;
    private final BlockedThirdPartyResourceAssembler blockAssembler;

    /**
     * Creates a new entry point for accessing the service that regards the responses to individual requests
     *
     * @param uploadResponseService service that manages the responses: needed in order to access
     *                              the business functions of the service
     * @param responseAssembler assembler for responses that adds hypermedia content (HAL)
     * @param blockAssembler assembler for blocks of third party customer that adds hypermedia content (HAL)
     */
    ResponseController(UploadResponseService uploadResponseService,
                       ResponseResourceAssembler responseAssembler,
                       BlockedThirdPartyResourceAssembler blockAssembler){
        this.responseAssembler = responseAssembler;
        this.uploadResponseService = uploadResponseService;
        this.blockAssembler = blockAssembler;
    }

    /**
     * Add a new response to a certain request.
     *
     * @param requestID id of the request that is replied with this call
     * @param ssn identified of the user that is performing the response
     * @param newResponse type of response (e.g. accept the request)
     * @return an http 201 created message that contains the newly formed link
     * @throws URISyntaxException due to the creation of a new URI resource
     */
    @PostMapping("/response/{ssn}/{requestID}")
    public @ResponseBody ResponseEntity<?> newResponse(@PathVariable Long requestID, @PathVariable
            String ssn, @RequestBody ResponseType newResponse) throws URISyntaxException {

        Resource<Response> resource = responseAssembler.toResource(uploadResponseService.addResponse(requestID, newResponse, new User(ssn)));

        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }

    /**
     * Add a block on a certain third party customer for a specific user
     *
     * @param ssn identified of the user that blocks the third party
     * @param thirdPartyID identified of the third party that will be blocked
     * @return an http 201 created message that contains the newly formed link
     * @throws URISyntaxException due to the creation of a new URI resource
     */
    @PostMapping("/blockedThirdParty/{ssn}/{thirdPartyID}")
    public @ResponseBody ResponseEntity<?> blockThirdParty(@PathVariable String ssn, @PathVariable Long thirdPartyID) throws URISyntaxException {
        Resource<BlockedThirdParty> resource = blockAssembler.toResource(uploadResponseService.addBlock(new User(ssn), thirdPartyID));
        return ResponseEntity.created(new URI(resource.getId().expand().getHref())).body(resource);
    }
}
