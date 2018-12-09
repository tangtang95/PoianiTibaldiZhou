package com.poianitibaldizhou.trackme.sharedataservice.message.protocol;

import com.poianitibaldizhou.trackme.sharedataservice.message.protocol.enumerator.IndividualRequestStatusProtocolMessage;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class IndividualRequestProtocolMessage {

    private Long id;
    private Long thirdPartyId;
    private IndividualRequestStatusProtocolMessage status;
    private Timestamp creationTimestamp;
    private Date startDate;
    private Date endDate;
    private String userSsn;

}
