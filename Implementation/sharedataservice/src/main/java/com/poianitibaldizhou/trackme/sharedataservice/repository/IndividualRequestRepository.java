package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.IndividualRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for accessing data regarding the individual request of a third party
 */
public interface IndividualRequestRepository extends JpaRepository<IndividualRequest, Long> {

}
