package com.belleange.mall.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.belleange.mall.component.CertificationGenerator;
import com.belleange.mall.dto.EmailCertificationResponse;
import com.belleange.mall.repository.CertificationNumberRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  // final 필드에 대한 생성자를 자동으로 생성합니다.
public class MailSendService {

    private final JavaMailSender mailSender;  // 이메일을 전송하기 위한 JavaMailSender
    private final CertificationNumberRepository certificationNumberRepository;  // 인증 번호를 저장하기 위한 Repository
    private final CertificationGenerator generator;  // 인증 번호를 생성하는 유틸리티 클래스

    // 인증 이메일을 생성하고 전송하는 메서드
    public EmailCertificationResponse sendEmailForCertification(String email) throws NoSuchAlgorithmException, MessagingException {
        // 인증 번호를 생성합니다.
        String certificationNumber = generator.createCertificationNumber();
        // 이메일 본문 내용을 생성합니다.
        String content = String.format("%s/api/v1/users/verify?certificationNumber=%s&email=%s   링크를 3분 이내에 클릭해주세요.", DOMAIN_NAME, certificationNumber, email);
        // 인증 번호를 Redis에 저장합니다.
        certificationNumberRepository.saveCertificationNumber(email, certificationNumber);
        // 이메일을 전송합니다.
        sendMail(email, content);
        // 인증 응답을 반환합니다.
        return new EmailCertificationResponse(email, certificationNumber);
    }

    // 이메일을 전송하는 메서드
    private void sendMail(String email, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(email);
        helper.setSubject(MAIL_TITLE_CERTIFICATION);  // 이메일 제목 설정
        helper.setText(content);  // 이메일 본문 설정
        mailSender.send(mimeMessage);  // 이메일 전송
    }

    private static final String DOMAIN_NAME = "http://yourdomain.com";  // 도메인 이름 (실제 값으로 대체 필요)
    private static final String MAIL_TITLE_CERTIFICATION = "이메일 인증";  // 이메일 제목 (실제 값으로 대체 필요)
}
