package com.belleange.mall.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.belleange.mall.repository.CertificationNumberRepository;
import com.belleange.mall.exception.EmailNotFoundException;
import com.belleange.mall.exception.InvalidCertificationNumberException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MailVerifyServiceTest {

    @Mock
    private CertificationNumberRepository certificationNumberRepository;

    @InjectMocks
    private MailVerifyService mailVerifyService;

    @Test
    public void testVerifyEmailSuccess() {
        // Arrange
        String email = "user1@aaa.com";
        String certificationNumber = "123456";
        when(certificationNumberRepository.hasKey(email)).thenReturn(true);
        when(certificationNumberRepository.getCertificationNumber(email)).thenReturn(certificationNumber);

        // Act
        mailVerifyService.verifyEmail(email, certificationNumber);

        // Assert
        verify(certificationNumberRepository, times(1)).removeCertificationNumber(email);
    }

    @Test
    public void testVerifyEmailFailure_InvalidNumber() {
        // Arrange
        String email = "user1@aaa.com";
        String certificationNumber = "123456";
        when(certificationNumberRepository.hasKey(email)).thenReturn(true);
        when(certificationNumberRepository.getCertificationNumber(email)).thenReturn("654321");

        // Act & Assert
        assertThrows(InvalidCertificationNumberException.class, () -> {
            mailVerifyService.verifyEmail(email, certificationNumber);
        });
    }

    @Test
    public void testVerifyEmailFailure_EmailNotFound() {
        // Arrange
        String email = "user1@aaa.com";
        String certificationNumber = "123456";
        when(certificationNumberRepository.hasKey(email)).thenReturn(false);

        // Act & Assert
        assertThrows(EmailNotFoundException.class, () -> {
            mailVerifyService.verifyEmail(email, certificationNumber);
        });
    }
}
