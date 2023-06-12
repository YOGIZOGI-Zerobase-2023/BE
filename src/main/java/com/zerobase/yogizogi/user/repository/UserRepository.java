package com.zerobase.yogizogi.user.repository;

import com.zerobase.yogizogi.user.domain.entity.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmailAuthKey(String emailAuythKey);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByPhoneNumber(String phoneNumber);

    Optional<AppUser> findByNickName(String nickName);

}
