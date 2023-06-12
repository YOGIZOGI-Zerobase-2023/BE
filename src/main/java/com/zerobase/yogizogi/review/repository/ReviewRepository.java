package com.zerobase.yogizogi.review.repository;

import com.zerobase.yogizogi.review.domain.entity.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByAccommodationId(Long AccommodationId, Pageable pageable);

    Optional<Review> findFirstByAccommodationId(Long AccomodationId);

}
