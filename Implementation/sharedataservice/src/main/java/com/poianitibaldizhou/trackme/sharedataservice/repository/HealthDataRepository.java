package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.HealthData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthDataRepository extends JpaRepository<HealthData, Long> {

}
