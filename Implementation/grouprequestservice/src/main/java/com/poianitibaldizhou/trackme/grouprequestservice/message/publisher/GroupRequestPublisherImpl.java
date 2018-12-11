package com.poianitibaldizhou.trackme.grouprequestservice.message.publisher;

import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.FilterStatementProtocolMessage;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.GroupRequestProtocolMessage;
import com.poianitibaldizhou.trackme.grouprequestservice.util.*;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.stream.Collectors;

public class GroupRequestPublisherImpl implements GroupRequestPublisher {

    private RabbitTemplate rabbitTemplate;

    private TopicExchange groupRequestExchange;

    public GroupRequestPublisherImpl(RabbitTemplate rabbitTemplate, TopicExchange groupRequestExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.groupRequestExchange = groupRequestExchange;
    }

    @Override
    public void publishGroupRequestCreated(GroupRequestWrapper groupRequestWrapper) {
        GroupRequestProtocolMessage groupRequestProtocolMessage = convertToGroupRequestProtocolMessage(groupRequestWrapper);
        rabbitTemplate.convertAndSend(groupRequestExchange.getName(), "grouprequest.event.created", groupRequestProtocolMessage);
    }

    @Override
    public void publishGroupRequestAccepted(GroupRequestWrapper groupRequestWrapper) {
        GroupRequestProtocolMessage groupRequestProtocolMessage = convertToGroupRequestProtocolMessage(groupRequestWrapper);
        rabbitTemplate.convertAndSend(groupRequestExchange.getName(), "grouprequest.event.accepted", groupRequestProtocolMessage);
    }

    private GroupRequestProtocolMessage convertToGroupRequestProtocolMessage(GroupRequestWrapper groupRequestWrapper){
        GroupRequestProtocolMessage groupRequestProtocolMessage = new GroupRequestProtocolMessage();
        groupRequestProtocolMessage.setId(groupRequestWrapper.getGroupRequest().getId());
        groupRequestProtocolMessage.setAggregatorOperator(AggregatorOperatorUtils
                .getAggregatorOperatorOfProtocol(groupRequestWrapper.getGroupRequest().getAggregatorOperator()));
        groupRequestProtocolMessage.setRequestType(RequestTypeUtils
                .getRequestTypeOfProtocol(groupRequestWrapper.getGroupRequest().getRequestType()));
        groupRequestProtocolMessage.setThirdPartyId(groupRequestWrapper.getGroupRequest().getThirdPartyId());
        groupRequestProtocolMessage.setCreationTimestamp(groupRequestWrapper.getGroupRequest().getCreationTimestamp());

        groupRequestProtocolMessage.setFilterStatements(groupRequestWrapper.getFilterStatementList().stream().map(filterStatement -> {
            FilterStatementProtocolMessage filterStatementProtocolMessage = new FilterStatementProtocolMessage();
            filterStatementProtocolMessage.setColumn(FieldTypeUtils.getFieldTypeOfProtocol(filterStatement.getColumn()));
            filterStatementProtocolMessage.setComparisonSymbol(ComparisonSymbolUtils
                    .getComparisonSymbolOfProtocol(filterStatement.getComparisonSymbol()));
            filterStatementProtocolMessage.setValue(filterStatement.getValue());
            return filterStatementProtocolMessage;
        }).collect(Collectors.toList()));

        return groupRequestProtocolMessage;
    }
}
