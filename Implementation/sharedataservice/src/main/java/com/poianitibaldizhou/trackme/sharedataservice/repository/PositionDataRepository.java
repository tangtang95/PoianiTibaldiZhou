package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.PositionData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionDataRepository extends JpaRepository<PositionData, Long> {
}
