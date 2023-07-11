package com.zerobase.yogizogi.accommodation.repository;

import com.zerobase.yogizogi.accommodation.domain.document.RoomDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RoomSearchRepository extends ElasticsearchRepository<RoomDocument, Long> {
}
