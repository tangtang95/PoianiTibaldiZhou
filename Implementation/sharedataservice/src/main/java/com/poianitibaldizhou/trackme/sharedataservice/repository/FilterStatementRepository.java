package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.entity.GroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilterStatementRepository extends JpaRepository<FilterStatement, Long> {

    List<FilterStatement> findAllByGroupRequest(GroupRequest groupRequest);
}
