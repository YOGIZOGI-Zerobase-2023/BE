package com.zerobase.yogizogi.accommodation.domain.model;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class PositionRequestForm {
    private double leftUpLat;
    private double rightDownLat;
    private double leftUpLon;
    private double rightDownLon;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
