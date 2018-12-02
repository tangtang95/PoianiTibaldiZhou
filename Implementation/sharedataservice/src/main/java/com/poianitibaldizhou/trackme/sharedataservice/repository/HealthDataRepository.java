package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for accessing data regarding the health data of a user
 */
public interface HealthDataRepository extends JpaRepository<HealthData, Long>, JpaSpecificationExecutor<HealthData> {

}
