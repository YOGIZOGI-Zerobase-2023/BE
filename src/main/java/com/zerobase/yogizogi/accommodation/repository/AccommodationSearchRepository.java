package com.zerobase.yogizogi.accommodation.repository;

import com.zerobase.yogizogi.accommodation.domain.document.AccommodationDocument;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
public interface AccommodationSearchRepository extends
    ElasticsearchRepository<AccommodationDocument, Long> {

    List<AccommodationDocument> findByAddressContaining(String query);

    List<AccommodationDocument> findByName(String query);
}
