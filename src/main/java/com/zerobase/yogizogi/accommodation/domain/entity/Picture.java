package com.zerobase.yogizogi.accommodation.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Picture {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "pictureId")
  private Long id;
  @Column(name = "url")
  private String url;
  @ManyToOne
  @JoinColumn(name = "accommodationId")
  private Accommodation accommodation;
}
