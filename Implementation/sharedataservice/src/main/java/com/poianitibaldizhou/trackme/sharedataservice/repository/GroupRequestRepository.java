package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.GroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for accessing data regarding the group request of a third party
 */
public interface GroupRequestRepository extends JpaRepository<GroupRequest, Long> {
}
