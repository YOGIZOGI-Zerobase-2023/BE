package com.zerobase.yogizogi.user.smtp.service;

import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.smtp.domain.model.MessageForm;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendMail(MessageForm messageForm) {//email 이 가지 않았을 때의 오류 메시지 필요.
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(messageForm.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(messageForm.getSubject()); // 메일 제목
            mimeMessageHelper.setText(messageForm.getMessage(), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);
            log.info("Success!!");
        } catch (CustomException | MessagingException e) {
            log.info("fail!!");
            throw new CustomException(ErrorCode.NOT_VALID_EMAIL);
        }
    }
}
