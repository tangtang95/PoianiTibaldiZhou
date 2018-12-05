package com.poianitibaldizhou.trackme.sharedataservice.entity;

import com.poianitibaldizhou.trackme.sharedataservice.util.IndividualRequestStatus;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * JPA entity object regarding the individual request
 */
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

    @ManyToOne
    @JoinColumn(name = "user_ssn", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long thirdPartyId;

}
