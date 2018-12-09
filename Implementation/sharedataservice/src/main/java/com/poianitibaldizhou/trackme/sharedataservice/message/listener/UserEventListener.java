package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.UserProtocolMessage;

public interface UserEventListener {

    /**
     * Called when a message regarding user created has been found on the message broker (rabbit-mq).
     * Based on the user protocol message, it converts it into a user of this service and save it on the DBs
     *
     * @param userProtocol the user protocol message from the message broker
     */
    void onUserCreated(UserProtocolMessage userProtocol);

}
