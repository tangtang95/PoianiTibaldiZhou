package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.GroupRequestProtocolMessage;

public interface GroupRequestEventListener {

    /**
     * Called when a message regarding group request created has been found on the message broker (rabbit-mq).
     * Based on the group request protocol message, makes a query to find out the user involved and publish it
     * on the number of user involved exchange
     *
     * @param groupRequestProtocol the group request protocol message from the message broker
     */
    void onGroupRequestCreated(GroupRequestProtocolMessage groupRequestProtocol);

    /**
     * Called when a message regarding group request accepted has been found on the message broker (rabbit-mq).
     * Based on the group request protocol message, it converts the message into the group request and filter
     * statements of this service and save it on the DBs
     *
     * @param groupRequestProtocol the group request protocol message from the message broker
     */
    void onGroupRequestAccepted(GroupRequestProtocolMessage groupRequestProtocol);

}
