package com.zerobase.yogizogi.accommodation.repository;

import com.zerobase.yogizogi.accommodation.domain.entity.Convenience;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvenienceRepository extends JpaRepository<Convenience, Long> {

    List<Convenience> findAllByAccommodationId(Long accommodationId);
}
