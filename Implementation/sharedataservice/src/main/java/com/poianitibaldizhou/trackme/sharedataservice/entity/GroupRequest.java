package com.poianitibaldizhou.trackme.sharedataservice.entity;

import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.sharedataservice.util.RequestType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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

}
