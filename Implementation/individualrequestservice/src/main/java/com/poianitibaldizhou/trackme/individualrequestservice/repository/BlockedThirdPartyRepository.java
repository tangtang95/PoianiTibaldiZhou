package com.poianitibaldizhou.trackme.individualrequestservice.repository;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdParty;
import com.poianitibaldizhou.trackme.individualrequestservice.entity.BlockedThirdPartyKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedThirdPartyRepository extends JpaRepository<BlockedThirdParty, BlockedThirdPartyKey> {

}
