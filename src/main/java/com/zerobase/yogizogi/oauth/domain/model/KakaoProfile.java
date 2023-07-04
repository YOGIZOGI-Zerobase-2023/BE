package com.zerobase.yogizogi.oauth.domain.model;

import lombok.Getter;

@Getter
public class KakaoProfile {

    private Long id;
    private String connected_at;

    private Properties properties;
    private KakaoAccount kakao_account;

    @Getter

    public static class KakaoAccount {

        private Boolean has_email;
        private Boolean email_needs_agreement;
        private Boolean is_email_valid;
        private Boolean is_email_verified;
        private String email;
        private boolean profile_nickname_needs_agreement;
        private Profile profile;
    }

    @Getter
    public static class Properties {

        private String nickname;
    }

    @Getter
    public static class Profile {

        private String nickname;
    }
}
