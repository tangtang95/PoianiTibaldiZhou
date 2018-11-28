package com.poianitibaldizhou.trackme.sharedataservice.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.poianitibaldizhou.trackme.sharedataservice.util.Views;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class PositionData {

    @JsonView(Views.Internal.class)
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
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
    private Double longitude;

    @JsonView(Views.Public.class)
    @Column(nullable = false)
    private Double latitude;

}
