package com.belleange.mall.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.belleange.mall.service.MailSendService;
import com.belleange.mall.service.MailVerifyService;
import com.belleange.mall.dto.EmailCertificationRequest;
import com.belleange.mall.dto.EmailCertificationResponse;
import com.belleange.mall.dto.ApiResponse;

import jakarta.mail.MessagingException;
import java.security.NoSuchAlgorithmException;

@RestController  // 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냅니다.
@RequiredArgsConstructor  // Lombok 어노테이션으로, 모든 final 필드에 대한 생성자를 자동으로 생성합니다.
@RequestMapping("/api/v1/mails")  // 기본 URL 경로를 "/api/v1/mails"로 설정합니다.
public class MailController {

    private final MailSendService mailSendService;  // 이메일 전송 서비스
    private final MailVerifyService mailVerifyService;  // 이메일 인증 서비스

    // 이메일 인증 번호를 전송하는 API 엔드포인트
    @PostMapping("/send-certification")
    public ResponseEntity<ApiResponse<EmailCertificationResponse>> sendCertificationNumber(
            @Validated @RequestBody EmailCertificationRequest request) throws MessagingException, NoSuchAlgorithmException {
        // 이메일 인증 번호를 생성하고 전송합니다.
        EmailCertificationResponse response = mailSendService.sendEmailForCertification(request.getEmail());
        // 성공적인 응답을 반환합니다.
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 이메일 인증 번호를 검증하는 API 엔드포인트
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyCertificationNumber(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "certificationNumber") String certificationNumber) {
        // 이메일과 인증 번호를 검증합니다.
        mailVerifyService.verifyEmail(email, certificationNumber);
        // 성공적인 응답을 반환합니다.
        return ResponseEntity.ok(ApiResponse.success());
    }
}
