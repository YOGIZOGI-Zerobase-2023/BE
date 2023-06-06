package com.zerobase.yogizogi.oauth.domain.dto;

import lombok.Getter;

@Getter
public class KakaoProfile {
    public Long id;
    public String connected_at;
    public KakaoAccount kakao_account;
    @Getter
    public class KakaoAccount {

            public Boolean has_email;
            public Boolean email_needs_agreement;
            public Boolean is_email_valid;
            public Boolean is_email_verified;

            public String email;
            public Boolean has_age_range;
            public Boolean age_range_needs_agreement;
            public String age_range;
        }
}
