package com.poianitibaldizhou.trackme.sharedataservice.message.publisher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Slf4j
public class NumberOfUserInvolvedDataPublisherImpl implements NumberOfUserInvolvedDataPublisher {

    private RabbitTemplate rabbitTemplate;

    private TopicExchange numberOfUserInvolvedExchange;

    public NumberOfUserInvolvedDataPublisherImpl(RabbitTemplate rabbitTemplate, TopicExchange doubleDataExchange){
        this.rabbitTemplate = rabbitTemplate;
        this.numberOfUserInvolvedExchange = doubleDataExchange;
    }

    @Override
    public void publishNumberOfUserInvolvedData(Double numberOfUserInvolved) {
        rabbitTemplate.convertAndSend(numberOfUserInvolvedExchange.getName(),"double-data.event.generated", numberOfUserInvolved);
        log.info("PUBLISHED NumberOfUserInvolvedData: " + numberOfUserInvolved);
    }
}
