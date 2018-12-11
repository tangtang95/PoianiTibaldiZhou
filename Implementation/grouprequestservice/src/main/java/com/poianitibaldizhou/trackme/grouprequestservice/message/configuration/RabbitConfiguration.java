package com.poianitibaldizhou.trackme.grouprequestservice.message.configuration;

import com.poianitibaldizhou.trackme.grouprequestservice.service.GroupRequestManagerServiceImpl;
import com.poianitibaldizhou.trackme.grouprequestservice.service.InternalCommunicationService;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("usage-message-broker")
@Configuration
public class RabbitConfiguration {

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory jsaFactory(ConnectionFactory connectionFactory,
                                                           SimpleRabbitListenerContainerFactoryConfigurer configurer,
                                                           RabbitTemplate rabbitTemplate) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    @Bean
    public CommandLineRunner setInternalCommunicationService(GroupRequestManagerServiceImpl groupRequestManagerService,
                                                             InternalCommunicationService internalCommunicationService){
        return args -> groupRequestManagerService.setInternalCommunicationService(internalCommunicationService);
    }
}
