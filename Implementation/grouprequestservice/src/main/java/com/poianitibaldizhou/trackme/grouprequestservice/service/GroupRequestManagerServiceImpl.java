package com.poianitibaldizhou.trackme.grouprequestservice.service;

import com.poianitibaldizhou.trackme.grouprequestservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.grouprequestservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.grouprequestservice.exception.BadOperatorRequestTypeException;
import com.poianitibaldizhou.trackme.grouprequestservice.exception.GroupRequestNotFoundException;
import com.poianitibaldizhou.trackme.grouprequestservice.repository.FilterStatementRepository;
import com.poianitibaldizhou.trackme.grouprequestservice.repository.GroupRequestRepository;
import com.poianitibaldizhou.trackme.grouprequestservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.grouprequestservice.util.GroupRequestWrapper;
import com.poianitibaldizhou.trackme.grouprequestservice.util.RequestStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the interface that provides the services for the management of the group request
 */
@Service
public class GroupRequestManagerServiceImpl implements GroupRequestManagerService {

    private GroupRequestRepository groupRequestRepository;
    private FilterStatementRepository filterStatementRepository;

    /**
     * Creates the manager of the group request service.
     * It needs some repository in order to make some operations on data (e.g. saving a new request)
     *
     * @param groupRequestRepository repository regarding the group requests, useful to manage requests
     * @param filterStatementRepository repository regarding the filter statements: since information contained here
     *                                  is related to group request, access is necessary
     */
    public GroupRequestManagerServiceImpl(GroupRequestRepository groupRequestRepository,
                                          FilterStatementRepository filterStatementRepository) {
        this.groupRequestRepository = groupRequestRepository;
        this.filterStatementRepository = filterStatementRepository;
    }

    @Override
    public GroupRequestWrapper getById(Long id) {
        GroupRequest groupRequest = groupRequestRepository.findById(id).orElseThrow(() -> new GroupRequestNotFoundException(id));
        List<FilterStatement> filterStatements = filterStatementRepository.findAllByGroupRequest_Id(id);
        return new GroupRequestWrapper(groupRequest, filterStatements);
    }

    @Override
    public List<GroupRequestWrapper> getByThirdPartyId(Long thirdPartyId) {
        List<GroupRequestWrapper> groupRequestWrappers = new ArrayList<>();

        List<GroupRequest> groupRequestList = groupRequestRepository.findAllByThirdPartyId(thirdPartyId);

        groupRequestList.forEach(groupRequest ->
            groupRequestWrappers.add(new GroupRequestWrapper(groupRequest, filterStatementRepository.findAllByGroupRequest_Id(groupRequest.getId())))
        );

        return groupRequestWrappers;
    }

    @Override
    public GroupRequestWrapper addGroupRequest(GroupRequestWrapper groupRequestWrapper) {
        if(!AggregatorOperator.isValidOperator(groupRequestWrapper.getGroupRequest().getAggregatorOperator(),
                groupRequestWrapper.getGroupRequest().getRequestType())) {
            throw new BadOperatorRequestTypeException(groupRequestWrapper.getGroupRequest().getAggregatorOperator(),
                    groupRequestWrapper.getGroupRequest().getRequestType());
        }

        groupRequestWrapper.getGroupRequest().setCreationTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        groupRequestWrapper.getGroupRequest().setStatus(RequestStatus.UNDER_ANALYSIS);

        GroupRequest savedRequest = groupRequestRepository.saveAndFlush(groupRequestWrapper.getGroupRequest());

        groupRequestWrapper.getFilterStatementList().forEach(filterStatement -> {
            filterStatement.setGroupRequest(savedRequest);
            filterStatementRepository.save(filterStatement);});

        filterStatementRepository.flush();

        return new GroupRequestWrapper(savedRequest, filterStatementRepository.findAllByGroupRequest_Id(savedRequest.getId()));
    }
}
