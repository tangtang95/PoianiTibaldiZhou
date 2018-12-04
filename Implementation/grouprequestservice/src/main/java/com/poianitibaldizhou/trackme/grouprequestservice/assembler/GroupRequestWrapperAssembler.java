package com.poianitibaldizhou.trackme.grouprequestservice.assembler;

import com.poianitibaldizhou.trackme.grouprequestservice.util.GroupRequestWrapper;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Component that facilitates the creation of resources regarding a group request
 */
@Component
public class GroupRequestWrapperAssembler implements ResourceAssembler<GroupRequestWrapper, Resource<GroupRequestWrapper>>{


    @Override
    public Resource<GroupRequestWrapper> toResource(GroupRequestWrapper groupRequestWrapper) {
        // TODO
        return null;
    }
}
