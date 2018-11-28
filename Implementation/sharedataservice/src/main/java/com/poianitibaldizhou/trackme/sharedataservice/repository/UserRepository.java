package com.poianitibaldizhou.trackme.sharedataservice.repository;

import com.poianitibaldizhou.trackme.sharedataservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
