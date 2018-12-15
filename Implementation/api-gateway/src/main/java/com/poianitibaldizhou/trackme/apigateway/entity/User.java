package com.poianitibaldizhou.trackme.apigateway.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

/**
 * JPA entity object regarding the user
 */
@Data
@Entity
public class User implements UserDetails {

    @JsonIgnore
    @Id
    @Column(length = 16)
    private String ssn;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(nullable = false, length = 64, unique = true)
    private String username;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
