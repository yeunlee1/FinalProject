package com.belleange.mall.controller;

import com.belleange.mall.dto.EmailCertificationRequest;
import com.belleange.mall.dto.EmailCertificationResponse;
import com.belleange.mall.service.MailSendService;
import com.belleange.mall.service.MailVerifyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class MailControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private MailSendService mailSendService;

    @MockBean
    private MailVerifyService mailVerifyService;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testSendCertificationNumber() throws Exception {
        EmailCertificationRequest request = new EmailCertificationRequest();
        request.setEmail("user1@aaa.com");
        EmailCertificationResponse response = new EmailCertificationResponse("user1@aaa.com", "123456");

        when(mailSendService.sendEmailForCertification(anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v1/mails/send-certification")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"user1@aaa.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testVerifyCertificationNumber() throws Exception {
        doNothing().when(mailVerifyService).verifyEmail(anyString(), anyString());

        mockMvc.perform(get("/api/v1/mails/verify")
                .param("email", "user1@aaa.com")
                .param("certificationNumber", "123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
