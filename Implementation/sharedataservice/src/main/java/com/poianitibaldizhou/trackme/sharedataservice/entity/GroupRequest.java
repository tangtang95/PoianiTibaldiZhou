package com.poianitibaldizhou.trackme.sharedataservice.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class GroupRequest {

    @Id
    @GeneratedValue
    private Long id;

}
