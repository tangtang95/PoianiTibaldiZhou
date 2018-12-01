package com.poianitibaldizhou.trackme.sharedataservice.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.sharedataservice.util.Views;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class HealthData {

    @Id
    @JsonView(Views.Internal.class)
    @GeneratedValue
    private Long id;

    @JsonView(Views.Internal.class)
    @ManyToOne
    @JoinColumn(name = "user_ssn", nullable = false)
    private User user;

    @JsonView(Views.Public.class)
    @Column(nullable = false)
    private Timestamp timestamp;

    @JsonView(Views.Public.class)
    @Column(nullable = false)
    private Integer heartBeat;

    @JsonView(Views.Public.class)
    @Column(nullable = false)
    private Integer bloodOxygenLevel;

    @JsonView(Views.Public.class)
    @Column(nullable = false)
    private Integer pressureMin;

    @JsonView(Views.Public.class)
    @Column(nullable = false)
    private Integer pressureMax;

}
