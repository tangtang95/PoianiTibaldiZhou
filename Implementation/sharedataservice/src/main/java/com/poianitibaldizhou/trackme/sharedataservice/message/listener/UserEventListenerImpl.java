package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.UserProtocolMessage;
import com.poianitibaldizhou.trackme.sharedataservice.repository.UserRepository;
import com.poianitibaldizhou.trackme.sharedataservice.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class UserEventListenerImpl implements UserEventListener {

    private final UserRepository userRepository;

    public UserEventListenerImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = Constants.USER_CREATED_SHARE_DATA_QUEUE_NAME)
    @Transactional
    @Override
    public void onUserCreated(@Payload UserProtocolMessage userProtocol) {
        log.info("BEFORE: onUserCreated " + userProtocol.toString());
        User user = new User();
        user.setSsn(userProtocol.getSsn());
        user.setFirstName(userProtocol.getFirstName());
        user.setLastName(userProtocol.getLastName());
        user.setBirthDate(userProtocol.getBirthDate());
        user.setBirthCity(userProtocol.getBirthCity());
        user.setBirthNation(userProtocol.getBirthNation());
        userRepository.save(user);
        log.info("AFTER: onUserCreated");
    }
}
