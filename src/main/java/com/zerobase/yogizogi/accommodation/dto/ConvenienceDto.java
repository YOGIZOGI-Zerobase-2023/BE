package com.zerobase.yogizogi.accommodation.dto;
import com.zerobase.yogizogi.accommodation.domain.entity.Convenience;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConvenienceDto {

    private final String facility;

    public static ConvenienceDto from(Convenience convenience) {
        return ConvenienceDto.builder()
            .facility(convenience.getFacility())
            .build();
    }

}
