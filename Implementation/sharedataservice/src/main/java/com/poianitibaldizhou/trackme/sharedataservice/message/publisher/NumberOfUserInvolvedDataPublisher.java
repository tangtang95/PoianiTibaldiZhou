package com.poianitibaldizhou.trackme.sharedataservice.message.publisher;

public interface NumberOfUserInvolvedDataPublisher {

    /**
     * Send the number of user involved to the message broker (rabbit-mq)
     *
     * @param numberOfUserInvolved the number of user involved of a specific group request
     */
    void publishNumberOfUserInvolvedData(Double numberOfUserInvolved);

}
