package com.belleange.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EmailCertificationResponse {
    
    private String email;
    private String certificationNumber;
}
