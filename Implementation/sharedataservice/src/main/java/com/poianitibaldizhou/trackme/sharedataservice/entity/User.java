package com.poianitibaldizhou.trackme.sharedataservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(length = 16)
    private String ssn;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Date birthDate;

}
