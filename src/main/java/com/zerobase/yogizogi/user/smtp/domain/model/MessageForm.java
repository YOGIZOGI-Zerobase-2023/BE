package com.zerobase.yogizogi.user.smtp.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageForm {

    private String to;

    private String subject;

    private String message;
}
