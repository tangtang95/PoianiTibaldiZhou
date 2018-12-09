package com.poianitibaldizhou.trackme.accountservice.assembler;

import com.poianitibaldizhou.trackme.accountservice.entity.User;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Component that facilitates the creation of resources regarding the user
 */
@Component
public class UserAssembler implements ResourceAssembler<User, Resource<User>>{

    @Override
    public Resource<User> toResource(User user) {
        // TODO
        return null;
    }
}
