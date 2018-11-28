package com.poianitibaldizhou.trackme.individualrequestservice.repository;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndividualRequestRepository extends JpaRepository<IndividualRequest, Long> {
    List<IndividualRequest> findAllByThirdPartyID(Long id);
}
