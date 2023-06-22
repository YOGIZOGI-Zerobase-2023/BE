package com.zerobase.yogizogi.accommodation.repository;

import com.zerobase.yogizogi.accommodation.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

}
