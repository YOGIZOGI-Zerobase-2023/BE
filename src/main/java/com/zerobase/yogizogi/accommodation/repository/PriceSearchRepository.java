package com.zerobase.yogizogi.accommodation.repository;

import com.zerobase.yogizogi.accommodation.domain.document.PriceDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PriceSearchRepository extends ElasticsearchRepository<PriceDocument, Long> {
}