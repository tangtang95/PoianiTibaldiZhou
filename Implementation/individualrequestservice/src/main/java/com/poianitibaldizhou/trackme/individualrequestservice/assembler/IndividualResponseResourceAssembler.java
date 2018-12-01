package com.poianitibaldizhou.trackme.individualrequestservice.assembler;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualResponse;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;

/**
 * Component that facilitates the creation of resources regarding the individual response
 */
public class IndividualResponseResourceAssembler implements ResourceAssembler<IndividualResponse, Resource<IndividualResponse>> {

    @Override
    public Resource<IndividualResponse> toResource(IndividualResponse response) {
        // TODO add self link to controller method
        return new Resource<>(response);
    }
}
