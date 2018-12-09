package com.poianitibaldizhou.trackme.accountservice.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * JPA entity object regarding the third party customer
 */
@Data
@Entity
public class ThirdPartyCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(length = 254, unique = true)
    private String email;

    @Column(nullable = false, length = 64)
    private String password;

}