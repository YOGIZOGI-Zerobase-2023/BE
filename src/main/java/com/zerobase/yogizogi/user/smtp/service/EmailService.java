package com.zerobase.yogizogi.user.smtp.service;

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

    public void mailSend(Long userId){

    }
    public void sendMail(MessageForm messageForm) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(messageForm.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(messageForm.getSubject()); // 메일 제목
            mimeMessageHelper.setText(messageForm.getMessage(), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);
            log.info("Success!!");
        } catch (MessagingException e) {
            log.info("fail!!");
            throw new RuntimeException(e);
        }
    }
}
