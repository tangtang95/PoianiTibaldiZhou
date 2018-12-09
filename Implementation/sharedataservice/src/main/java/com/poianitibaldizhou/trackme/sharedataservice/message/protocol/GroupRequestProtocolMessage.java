package com.poianitibaldizhou.trackme.sharedataservice.message.protocol;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.AggregatorOperatorProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.GroupRequestStatusProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.RequestTypeProtocolMessage;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class GroupRequestProtocolMessage {

    private Long id;
    private Long thirdPartyId;
    private GroupRequestStatusProtocolMessage status;
    private Timestamp creationTimestamp;
    private AggregatorOperatorProtocolMessage aggregatorOperator;
    private RequestTypeProtocolMessage requestType;
    private List<FilterStatementProtocolMessage> filterStatements;

}
