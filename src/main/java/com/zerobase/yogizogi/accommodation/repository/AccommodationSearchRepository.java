package com.zerobase.yogizogi.accommodation.repository;

import com.zerobase.yogizogi.accommodation.domain.document.AccommodationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AccommodationSearchRepository extends
    ElasticsearchRepository<AccommodationDocument, Long> {
}
