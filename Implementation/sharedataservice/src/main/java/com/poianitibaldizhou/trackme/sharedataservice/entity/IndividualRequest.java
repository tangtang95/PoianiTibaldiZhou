package com.poianitibaldizhou.trackme.sharedataservice.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class IndividualRequest {

    @Id
    @GeneratedValue
    private Long id;

}
