package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Repository for accessing data regarding the position data of a user
 */
public interface PositionDataRepository extends JpaRepository<PositionData, Long>, JpaSpecificationExecutor<PositionData> {

}
