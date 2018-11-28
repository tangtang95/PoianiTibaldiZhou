package com.poianitibaldizhou.trackme.individualrequestservice.entity;

import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestResponse;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class IndividualResponse {
    @Id
    private long requestID;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IndividualRequestResponse response;

    @Column(nullable = false)
    private Timestamp acceptanceDate;
}