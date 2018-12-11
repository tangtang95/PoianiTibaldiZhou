package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.sharedataservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.GroupRequestProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.GroupRequestStatusProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.publisher.NumberOfUserInvolvedDataPublisher;
import com.poianitibaldizhou.trackme.sharedataservice.repository.FilterStatementRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.GroupRequestRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.UserRepository;
import com.poianitibaldizhou.trackme.sharedataservice.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class GroupRequestEventListenerImpl implements GroupRequestEventListener {

    private final UserRepository userRepository;
    private final FilterStatementRepository filterStatementRepository;
    private final GroupRequestRepository groupRequestRepository;
    private final NumberOfUserInvolvedDataPublisher numberOfUserInvolvedDataPublisher;

    public GroupRequestEventListenerImpl(UserRepository userRepository,
                                         GroupRequestRepository groupRequestRepository,
                                         FilterStatementRepository filterStatementRepository,
                                         NumberOfUserInvolvedDataPublisher numberOfUserInvolvedDataPublisher){
        this.userRepository = userRepository;
        this.groupRequestRepository = groupRequestRepository;
        this.filterStatementRepository = filterStatementRepository;
        this.numberOfUserInvolvedDataPublisher = numberOfUserInvolvedDataPublisher;
    }

    @RabbitListener(queues = Constants.GROUP_REQUEST_CREATED_SHARE_DATA_QUEUE_NAME)
    @Transactional
    @Override
    public void onGroupRequestCreated(@Payload GroupRequestProtocolMessage groupRequestProtocol) {
        log.info("BEFORE: onGroupRequestCreated " + groupRequestProtocol.toString());
        if(!GroupRequestProtocolMessage.validateMessage(groupRequestProtocol)){
            log.error("FATAL ERROR: Received a group request which has not all attributes non-null " + groupRequestProtocol);
            return;
        }
        if(!groupRequestProtocol.getStatus().equals(GroupRequestStatusProtocolMessage.UNDER_ANALYSIS)) {
            log.error("FATAL ERROR: Received a group request which is not UNDER_ANALYSIS " + groupRequestProtocol);
            return;
        }
        AggregatorOperator distinctCountOperator = AggregatorOperator.DISTINCT_COUNT;
        RequestType userSsnRequestType = RequestType.USER_SSN;
        List<FilterStatement> filterStatementList = groupRequestProtocol.getFilterStatements().stream().map(filterStatementProtocol->{
            FilterStatement filterStatement = new FilterStatement();
            filterStatement.setColumn(FieldTypeUtils.getFieldType(filterStatementProtocol.getColumn()));
            filterStatement.setComparisonSymbol(ComparisonSymbolUtils
                    .getComparisonSymbol(filterStatementProtocol.getComparisonSymbol()));
            filterStatement.setValue(filterStatementProtocol.getValue());
            return filterStatement;
        }).collect(Collectors.toList());
        Double numberOfUserInvolved = userRepository.getAggregatedData(distinctCountOperator,
                userSsnRequestType, filterStatementList);
        numberOfUserInvolvedDataPublisher.publishNumberOfUserInvolvedData(numberOfUserInvolved);
        log.info("AFTER: onGroupRequestCreated");
    }

    @RabbitListener(queues = Constants.GROUP_REQUEST_ACCEPTED_SHARE_DATA_QUEUE_NAME)
    @Transactional
    @Override
    public void onGroupRequestAccepted(@Payload GroupRequestProtocolMessage groupRequestProtocol) {
        log.info("BEFORE: onGroupRequestAccepted " + groupRequestProtocol.toString());
        if(!GroupRequestProtocolMessage.validateMessage(groupRequestProtocol)){
            log.error("FATAL ERROR: Received a group request which has not all attributes non-null " + groupRequestProtocol);
            return;
        }
        if(!groupRequestProtocol.getStatus().equals(GroupRequestStatusProtocolMessage.ACCEPTED)) {
            log.error("FATAL ERROR: Received a group request which is not ACCEPTED " + groupRequestProtocol);
            return;
        }
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId(groupRequestProtocol.getId());
        groupRequest.setCreationTimestamp(groupRequestProtocol.getCreationTimestamp());
        groupRequest.setAggregatorOperator(AggregatorOperatorUtils
                .getAggregatorOperator(groupRequestProtocol.getAggregatorOperator()));
        groupRequest.setRequestType(RequestTypeUtils.getRequestType(groupRequestProtocol.getRequestType()));
        groupRequest.setThirdPartyId(groupRequestProtocol.getThirdPartyId());
        groupRequestRepository.save(groupRequest);
        groupRequestProtocol.getFilterStatements().forEach(filterStatementProtocol -> {
            FilterStatement filterStatement = new FilterStatement();
            filterStatement.setColumn(FieldTypeUtils.getFieldType(filterStatementProtocol.getColumn()));
            filterStatement.setComparisonSymbol(ComparisonSymbolUtils
                    .getComparisonSymbol(filterStatementProtocol.getComparisonSymbol()));
            filterStatement.setValue(filterStatementProtocol.getValue());
            filterStatement.setGroupRequest(groupRequest);
            filterStatementRepository.save(filterStatement);
        });
        log.info("AFTER: onGroupRequestAccepted");
    }
}
