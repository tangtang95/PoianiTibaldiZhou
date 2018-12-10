package com.poianitibaldizhou.trackme.accountservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

/**
 * JPA entity object regarding the user
 */
@Data
@Entity
public class User {

    @JsonIgnore
    @Id
    @Column(length = 16)
    private String ssn;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(nullable = false, length = 64, unique = true)
    private String userName;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false)
    private Date birthDate;

    @Column(nullable = false, length = 150)
    private String birthCity;

    @Column(nullable = false, length = 100)
    private String birthNation;
}
