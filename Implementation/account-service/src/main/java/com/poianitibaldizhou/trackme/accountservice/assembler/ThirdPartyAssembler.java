package com.poianitibaldizhou.trackme.accountservice.assembler;

import com.poianitibaldizhou.trackme.accountservice.util.ThirdPartyWrapper;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Component that facilitates the creation of resources regarding third party customers
 */
@Component
public class ThirdPartyAssembler implements ResourceAssembler<ThirdPartyWrapper, Resource<ThirdPartyWrapper>>{

    @Override
    public Resource<ThirdPartyWrapper> toResource(ThirdPartyWrapper thirdPartyWrapper) {
        // TODO
        return null;
    }
}
