package com.poianitibaldizhou.trackme.grouprequestservice.message.publisher;

import com.poianitibaldizhou.trackme.grouprequestservice.util.GroupRequestWrapper;

public interface GroupRequestPublisher {

    void publishGroupRequestCreated(GroupRequestWrapper groupRequestWrapper);

    void publishGroupRequestAccepted(GroupRequestWrapper groupRequestWrapper);

}
