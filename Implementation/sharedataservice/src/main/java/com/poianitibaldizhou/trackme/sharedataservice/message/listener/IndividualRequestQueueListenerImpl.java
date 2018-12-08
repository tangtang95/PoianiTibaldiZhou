package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import com.poianitibaldizhou.trackme.sharedataservice.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.IndividualRequestProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.repository.IndividualRequestRepository;
import com.poianitibaldizhou.trackme.sharedataservice.repository.UserRepository;
import com.poianitibaldizhou.trackme.sharedataservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class IndividualRequestQueueListenerImpl implements IndividualRequestQueueListener{

    private final UserRepository userRepository;
    private final IndividualRequestRepository individualRequestRepository;

    public IndividualRequestQueueListenerImpl(UserRepository userRepository,
                                              IndividualRequestRepository individualRequestRepository){
        this.userRepository = userRepository;
        this.individualRequestRepository = individualRequestRepository;
    }

    @RabbitListener(queues = Constants.INDIVIDUAL_REQUEST_ACCEPTED_SHARE_DATA_QUEUE_NAME)
    @Transactional
    @Override
    public void onIndividualRequestAccepted(@Payload IndividualRequestProtocolMessage individualRequestProtocol) {
        log.info("BEFORE: onIndividualRequestAccepted " + individualRequestProtocol.toString());
        User user = userRepository.findById(individualRequestProtocol.getUserSsn())
                .orElseThrow(() -> new UserNotFoundException(individualRequestProtocol.getUserSsn()));
        IndividualRequest individualRequest = new IndividualRequest();
        individualRequest.setId(individualRequestProtocol.getId());
        individualRequest.setThirdPartyId(individualRequestProtocol.getThirdPartyId());
        individualRequest.setUser(user);
        individualRequest.setStartDate(individualRequestProtocol.getStartDate());
        individualRequest.setEndDate(individualRequestProtocol.getEndDate());
        individualRequest.setCreationTimestamp(individualRequestProtocol.getCreationTimestamp());
        individualRequestRepository.save(individualRequest);
        log.info("AFTER: onIndividualRequestAccepted");
    }
}
