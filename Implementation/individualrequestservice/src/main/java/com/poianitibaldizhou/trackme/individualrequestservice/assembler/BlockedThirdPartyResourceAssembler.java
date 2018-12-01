package com.poianitibaldizhou.trackme.individualrequestservice.assembler;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdParty;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Component that facilitates the creation of resources regarding the block of third party customers
 */
@Component
public class BlockedThirdPartyResourceAssembler implements ResourceAssembler<BlockedThirdParty, Resource<BlockedThirdParty>> {
    @Override
    public Resource<BlockedThirdParty> toResource(BlockedThirdParty blockedThirdParty) {
        // TODO
        return null;
    }
}
