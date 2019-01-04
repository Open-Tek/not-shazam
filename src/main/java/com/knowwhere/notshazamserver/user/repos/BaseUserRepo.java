package com.knowwhere.notshazamserver.user.repos;

import com.knowwhere.notshazamserver.user.models.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseUserRepo extends JpaRepository<BaseUser, Long> {
    BaseUser findByEmailOrPhone(String email, String phone);
    //BaseUser findByPhone(String phone);
    Optional<BaseUser> findByEmailAndPassword(String email, String password);
    Optional<BaseUser> findByPhoneAndPassword(String phone, String password);
}
