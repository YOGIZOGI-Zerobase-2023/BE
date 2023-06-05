package com.zerobase.yogizogi.user.repository;

import com.zerobase.yogizogi.user.domain.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser,Long> {

}
