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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mails")
public class MailController {

    private final MailSendService mailSendService;
    private final MailVerifyService mailVerifyService;

    @PostMapping("/send-certification")
    public ResponseEntity<ApiResponse<EmailCertificationResponse>> sendCertificationNumber(
            @Validated @RequestBody EmailCertificationRequest request) throws MessagingException, NoSuchAlgorithmException {
        EmailCertificationResponse response = mailSendService.sendEmailForCertification(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyCertificationNumber(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "certificationNumber") String certificationNumber) {
        mailVerifyService.verifyEmail(email, certificationNumber);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
