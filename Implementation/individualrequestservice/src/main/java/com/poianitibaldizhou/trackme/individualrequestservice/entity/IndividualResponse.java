package com.poianitibaldizhou.trackme.individualrequestservice.entity;

import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestResponse;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Information regarding a response that an user provide w.r.t. a certain request that he has received
 */
@Data
@Entity
public class IndividualResponse implements Serializable {

    @Id
    @Column(name = "requestID")
    private Long requestID;

    @MapsId
    @OneToOne(mappedBy = "response")
    @JoinColumn(name = "requestID")
    private IndividualRequest request;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IndividualRequestResponse response;

    @Column(nullable = false)
    private Timestamp acceptanceTimeStamp;

    @JoinColumn(name = "ssn", nullable = false)
    @ManyToOne
    private User ssn;
}