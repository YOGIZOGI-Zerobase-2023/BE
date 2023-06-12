package com.zerobase.yogizogi.accommodation.service;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.model.AccommodationForm;
import com.zerobase.yogizogi.accommodation.repository.AccommodationRepository;
import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.book.repository.BookRepository;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.common.UserRole;
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

        //HOST 만 등록 가능.
        if (user.getUserRole() == UserRole.USER) {
            throw new CustomException(ErrorCode.USER_NOT_ALLOW_ACCESS);
        }

        Accommodation accommodation = Accommodation.builder().name(form.getName()).build();
        accommodationRepository.save(accommodation);
        return "/success";
    }
}
