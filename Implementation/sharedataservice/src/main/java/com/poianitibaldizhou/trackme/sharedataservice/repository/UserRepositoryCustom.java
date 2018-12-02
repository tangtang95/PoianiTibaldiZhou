package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.entity.GroupRequest;

import java.util.List;

/**
 * Custom repository for accessing aggregate data regarding a group request or an individual request on users
 */
public interface UserRepositoryCustom {

    /**
     * 
     * @param request
     * @param filters
     * @return
     */
    Double complexUnionQuery(GroupRequest request, List<FilterStatement> filters);

}
