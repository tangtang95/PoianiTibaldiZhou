package com.poianitibaldizhou.trackme.grouprequestservice.message.publisher;

import com.poianitibaldizhou.trackme.grouprequestservice.util.GroupRequestWrapper;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class GroupRequestPublisherImpl implements GroupRequestPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange groupRequestExchange;

    @Override
    public void publishGroupRequestCreated(GroupRequestWrapper groupRequestWrapper) {

    }

    @Override
    public void publishGroupRequestAccepted(GroupRequestWrapper groupRequestWrapper) {

    }
}
