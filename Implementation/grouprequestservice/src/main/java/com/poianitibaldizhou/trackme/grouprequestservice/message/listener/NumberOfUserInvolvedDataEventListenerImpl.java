package com.poianitibaldizhou.trackme.grouprequestservice.message.listener;

import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.NumberOfUserInvolvedProtocolMessage;
import com.poianitibaldizhou.trackme.grouprequestservice.util.Constants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.transaction.annotation.Transactional;

public class NumberOfUserInvolvedDataEventListenerImpl implements NumberOfUserInvolvedDataEventListener{

    @RabbitListener(queues = Constants.NUMBER_OF_USER_INVOLVED_GENERATED_GROUP_REQUEST_QUEUE_NAME)
    @Transactional
    @Override
    public void onNumberOfUserInvolvedDataGenerated(NumberOfUserInvolvedProtocolMessage protocolMessage) {

    }
}
