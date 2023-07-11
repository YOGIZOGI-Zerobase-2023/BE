package com.zerobase.yogizogi.accommodation.service;

import com.zerobase.yogizogi.accommodation.domain.document.AccommodationDocument;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.repository.AccommodationRepository;
import com.zerobase.yogizogi.accommodation.repository.AccommodationSearchRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticSearchService {

    private final AccommodationSearchRepository accommodationSearchRepository;

    public List<AccommodationDocument> searchAccommodation(String query) {
        // Search in Elasticsearch by address
        List<AccommodationDocument> results = accommodationSearchRepository.findByAddressContaining(query);

        // If no results, search by name
        if (results.isEmpty()) {
            results = accommodationSearchRepository.findByName(query);
        }

        return results;
    }
}
