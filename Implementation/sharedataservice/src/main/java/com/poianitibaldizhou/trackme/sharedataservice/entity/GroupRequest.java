package com.poianitibaldizhou.trackme.sharedataservice.entity;

import com.poianitibaldizhou.trackme.sharedataservice.util.AggregatorOperator;
import com.poianitibaldizhou.trackme.sharedataservice.util.RequestType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * JPA entity object regarding the group request
 */
@Data
@Entity
public class GroupRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long thirdPartyId;

    @Column(nullable = false)
    private Date date;

    @Column(length = 20, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AggregatorOperator aggregatorOperator = AggregatorOperator.COUNT;

    @Column(length = 20, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RequestType requestType;

}
