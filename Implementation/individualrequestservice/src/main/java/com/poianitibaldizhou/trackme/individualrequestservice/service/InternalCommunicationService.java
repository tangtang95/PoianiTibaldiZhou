package com.poianitibaldizhou.trackme.individualrequestservice.service;

import com.poianitibaldizhou.trackme.individualrequestservice.entity.IndividualRequest;
import com.poianitibaldizhou.trackme.individualrequestservice.message.protocol.UserProtocolMessage;

public interface InternalCommunicationService {

    /**
     * Handle the user protocol message obtained by the listener. It saves the user ssn in the DB
     *
     * @param userProtocolMessage the protocol message of group request
     */
    void handleUserCreated(UserProtocolMessage userProtocolMessage);

    /**
     * Send an individual request message through the message broker with the help of publishers
     *
     * @param individualRequest the individual request to be sent
     */
    void sendIndividualRequest(IndividualRequest individualRequest);

}
