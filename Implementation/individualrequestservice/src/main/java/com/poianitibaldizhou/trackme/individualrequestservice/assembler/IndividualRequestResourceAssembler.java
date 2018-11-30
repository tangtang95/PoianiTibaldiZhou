package com.poianitibaldizhou.trackme.individualrequestservice.assembler;

import com.poianitibaldizhou.trackme.individualrequestservice.controller.IndividualRequestController;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Component that facilitates the creation of resources regarding the individual request
 */
@Component
public class IndividualRequestResourceAssembler implements ResourceAssembler<IndividualRequest, Resource<IndividualRequest>> {


    @Override
    public Resource<IndividualRequest> toResource(IndividualRequest individualRequest) {
        return new Resource<>(individualRequest,
                linkTo(methodOn(IndividualRequestController.class).getRequestById(individualRequest.getId())).withSelfRel(),
                linkTo(methodOn(IndividualRequestController.class).getThirdPartyRequests(individualRequest.getThirdPartyID())).withRel("thirdPartyRequest"),
                linkTo(methodOn(IndividualRequestController.class).getUserPendingRequests(individualRequest.getSsn())).withRel("userPendingRequest"));
    }
}
