package com.poianitibaldizhou.trackme.sharedataservice.message.listener;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.IndividualRequestProtocolMessage;

public interface IndividualRequestQueueListener {

    /**
     * Called when a message regarding individual request accepted has been found on the message broker (rabbit-mq).
     * Based on the individual request protocol message, it converts it into an individual request of this
     * service and save it on the DBs
     *
     * @param individualRequestProtocol the individual request protocol message from the message broker
     */
    void onIndividualRequestAccepted(IndividualRequestProtocolMessage individualRequestProtocol);

}
