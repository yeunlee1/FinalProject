package com.belleange.mall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.belleange.mall.exception.EmailNotFoundException;
import com.belleange.mall.exception.InvalidCertificationNumberException;
import com.belleange.mall.repository.CertificationNumberRepository;

@Service
@RequiredArgsConstructor
public class MailVerifyService {

    private final CertificationNumberRepository certificationNumberRepository;

    public void verifyEmail(String email, String certificationNumber) {
        if (!isVerify(email, certificationNumber)) {
            throw new InvalidCertificationNumberException();
        }
        certificationNumberRepository.removeCertificationNumber(email);
    }

    private boolean isVerify(String email, String certificationNumber) {
        boolean validatedEmail = isEmailExists(email);
        if (!isEmailExists(email)) {
            throw new EmailNotFoundException();
        }
        return (validatedEmail &&
                certificationNumberRepository.getCertificationNumber(email).equals(certificationNumber));
    }

    private boolean isEmailExists(String email) {
        return certificationNumberRepository.hasKey(email);
    }
}
