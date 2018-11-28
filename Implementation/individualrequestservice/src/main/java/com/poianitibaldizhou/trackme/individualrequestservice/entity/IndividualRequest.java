package com.poianitibaldizhou.trackme.individualrequestservice.entity;

import com.poianitibaldizhou.trackme.individualrequestservice.util.IndividualRequestStatus;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
public class IndividualRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IndividualRequestStatus status;

    @Column(nullable = false)
    private Timestamp timestamp;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false, length = 16)
    private String ssn;

    @Column(nullable = false)
    private Long thirdPartyID;

    public IndividualRequest() {

    }

    public IndividualRequest(Timestamp timestamp, Date startDate, Date endDate, String ssn, Long thirdPartyID) {
        this.status = IndividualRequestStatus.PENDING;
        this.timestamp = timestamp;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ssn = ssn;
        this.thirdPartyID = thirdPartyID;
    }
}
