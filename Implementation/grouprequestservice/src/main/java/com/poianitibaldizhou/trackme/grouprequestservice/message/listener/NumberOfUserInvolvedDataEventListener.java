package com.poianitibaldizhou.trackme.grouprequestservice.message.listener;

import com.poianitibaldizhou.trackme.grouprequestservice.message.protocol.NumberOfUserInvolvedProtocolMessage;

public interface NumberOfUserInvolvedDataEventListener {

    void onNumberOfUserInvolvedDataGenerated(NumberOfUserInvolvedProtocolMessage protocolMessage);

}
