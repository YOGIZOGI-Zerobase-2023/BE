package com.zerobase.yogizogi.accommodation.service;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.model.AccommodationForm;
import com.zerobase.yogizogi.accommodation.dto.AccommodationDto;
import com.zerobase.yogizogi.accommodation.repository.AccommodationRepository;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.dto.UserDto;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationService {
    private final JwtAuthenticationProvider provider;
    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;

    public String makeAccommodation(String token, AccommodationForm form) {
        if (!provider.validateToken(token)) {
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }

        UserDto userDto = provider.getUserDto(token);
        AppUser user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        //TO DO Form 구체화**
        //불필요할 수 있음 추후 리팩토링 과제

        Accommodation accommodation = Accommodation.builder().name(form.getName()).build();
        accommodationRepository.save(accommodation);
        return "/success";
    }

    // TEST
    // 숙소 조회
    public AccommodationDto getAccommodation(Long id) {
        Accommodation accommodation = accommodationRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOMMODATION));

        // TO-DO
        // price 결측치에 대한 처리 필요 -> price, room을 list -> set으로 변경하여 get() 사용 불가
        return AccommodationDto.builder()
            .id(accommodation.getId())
            .name(accommodation.getName())
            .category(accommodation.getCategory())
            .address(accommodation.getAddress())
            .score(accommodation.getScore())
            .picUrl(accommodation.getPicUrl())
            .lon(accommodation.getLng())
            .lat(accommodation.getLat())
            .price(accommodation.getRooms().get(0).getPrices().stream().findFirst().get().getPrice())
            .build();
    }
}
