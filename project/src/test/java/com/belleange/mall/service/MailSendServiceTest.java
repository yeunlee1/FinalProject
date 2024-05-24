package com.belleange.mall.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.belleange.mall.component.CertificationGenerator;
import com.belleange.mall.repository.CertificationNumberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.security.NoSuchAlgorithmException;

@ExtendWith(MockitoExtension.class)
public class MailSendServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private CertificationNumberRepository certificationNumberRepository;

    @Mock
    private CertificationGenerator generator;

    @InjectMocks
    private MailSendService mailSendService;

    @Test
    public void testSendEmailForCertification() throws NoSuchAlgorithmException, MessagingException {
        // Arrange
        String email = "user1@aaa.com";
        String certificationNumber = "123456";
        when(generator.createCertificationNumber()).thenReturn(certificationNumber);
        doNothing().when(certificationNumberRepository).saveCertificationNumber(anyString(), anyString(), anyLong());

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        mailSendService.sendEmailForCertification(email);

        // Assert
        verify(generator, times(1)).createCertificationNumber();
        verify(certificationNumberRepository, times(1)).saveCertificationNumber(email, certificationNumber, 180);

        ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender, times(1)).send(mimeMessageCaptor.capture());

        MimeMessage capturedMimeMessage = mimeMessageCaptor.getValue();
        // 추가 검증 로직을 여기에 추가할 수 있습니다 (예: 이메일 주소, 제목, 본문 내용 등)
    }
}
