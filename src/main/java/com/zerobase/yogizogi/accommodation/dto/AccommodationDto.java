package com.zerobase.yogizogi.accommodation.dto;

import com.zerobase.yogizogi.accommodation.domain.entity.Picture;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationDto {
  private String name;
  private double score;
  private String picUrl;
  private String address;
  private int price;
}
