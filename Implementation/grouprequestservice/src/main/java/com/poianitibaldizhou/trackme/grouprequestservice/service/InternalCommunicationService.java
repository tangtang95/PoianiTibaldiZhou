package com.poianitibaldizhou.trackme.grouprequestservice.service;

import com.poianitibaldizhou.trackme.grouprequestservice.entity.FilterStatement;
import com.poianitibaldizhou.trackme.grouprequestservice.entity.GroupRequest;
import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.NumberOfUserInvolvedProtocolMessage;

import java.util.List;

public interface InternalCommunicationService {

    void handleNumberOfUserInvolvedData(NumberOfUserInvolvedProtocolMessage protocolMessage);

    void sendGroupRequestCreatedMessage(GroupRequest groupRequest, List<FilterStatement> filterStatementList);
}
