package com.poianitibaldizhou.trackme.grouprequestservice.util;

import com.poianitibaldizhou.trackme.grouprequestservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.grouprequestservice.entity.GroupRequest;

import java.io.Serializable;
import java.util.List;

/**
 * This class wraps all the information regarding the group request: indeed, a group request
 * and all the filter statement associated with it are present
 */
public class GroupRequestWrapper implements Serializable {

    private GroupRequest groupRequest;
    private List<FilterStatement> filterStatementList;

    /**
     * Creates a new group request with a group request and all the filter associated with it
     * This constructor checks the conditions that no filter statement related with other request are present in the
     * list: note that this may throw an illegal argument exception.
     *
     * @param groupRequest group request that will be wrapped
     * @param filterStatementList filter statement associated with it
     */
    public GroupRequestWrapper(GroupRequest groupRequest, List<FilterStatement> filterStatementList) {
        if (filterStatementList.stream().map(filterStatement -> filterStatement.getGroupRequest().getId())
                .anyMatch(requestID -> !requestID.equals(groupRequest.getId())))
            throw new IllegalArgumentException();

        this.groupRequest = groupRequest;
        this.filterStatementList = filterStatementList;
    }

    /**
     * Empty constructor for (de)serialization
     */
    public GroupRequestWrapper() {

    }

    /**
     * @return group request wrapped by this
     */
    public GroupRequest getGroupRequest() {
        return groupRequest;
    }

    /**
     * @return list of filter statement related to the group request that is embedded in this
     */
    public List<FilterStatement> getFilterStatementList() {
        return filterStatementList;
    }
}
