package com.belleange.mall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.belleange.mall.repository.CertificationNumberRepository;
import com.belleange.mall.exception.InvalidCertificationNumberException;
import com.belleange.mall.exception.EmailNotFoundException;

@Service
@RequiredArgsConstructor
public class MailVerifyService {

    private final CertificationNumberRepository certificationNumberRepository;  // 인증 번호를 저장하기 위한 Repository

    // 이메일 인증을 검증하는 메서드
    public void verifyEmail(String email, String certificationNumber) {
        if (!isVerify(email, certificationNumber)) {
            throw new InvalidCertificationNumberException();
        }
        certificationNumberRepository.removeCertificationNumber(email);
    }

    // 이메일과 인증 번호를 검증하는 메서드
    private boolean isVerify(String email, String certificationNumber) {
        boolean validatedEmail = isEmailExists(email);
        if (!validatedEmail) {
            throw new EmailNotFoundException();
        }
        return certificationNumberRepository.getCertificationNumber(email).equals(certificationNumber);
    }

    // 이메일이 존재하는지 확인하는 메서드
    private boolean isEmailExists(String email) {
        return certificationNumberRepository.hasKey(email);
    }
}
