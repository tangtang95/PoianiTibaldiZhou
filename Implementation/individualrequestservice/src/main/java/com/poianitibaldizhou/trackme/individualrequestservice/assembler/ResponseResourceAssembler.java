package com.poianitibaldizhou.trackme.individualrequestservice.assembler;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.Response;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Component that facilitates the creation of resources regarding the individual response
 */
@Component
public class ResponseResourceAssembler implements ResourceAssembler<Response, Resource<Response>> {

    @Override
    public Resource<Response> toResource(Response response) {
        // TODO add self link to controller method
        return new Resource<>(response);
    }
}
