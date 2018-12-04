package com.poianitibaldizhou.trackme.grouprequestservice.entity;

import com.poianitibaldizhou.trackme.grouprequestservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.grouprequestservice.util.RequestStatus;
import com.poianitibaldizhou.trackme.grouprequestservice.util.RequestType;
import lombok.Data;

import javax.persistence.*;

/**
 * JPA entity object regarding the group request
 */
@Data
@Entity
public class GroupRequest {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long thirdPartyId;

    @Column(length = 10, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AggregatorOperator aggregatorOperator = AggregatorOperator.COUNT;

    @Column(length = 20, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RequestType requestType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;
}
