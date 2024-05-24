package com.belleange.mall.component;

import org.springframework.stereotype.Component;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component  // 스프링이 이 클래스를 빈으로 관리하도록 지정합니다.
public class CertificationGenerator {

    // 인증 번호를 생성하는 메서드
    public String createCertificationNumber() throws NoSuchAlgorithmException {
        String result;

        do {
            int num = SecureRandom.getInstanceStrong().nextInt(999999);  // 0에서 999999 사이의 난수를 생성합니다.
            result = String.valueOf(num);
        } while (result.length() != 6);  // 결과가 6자리 숫자가 될 때까지 반복합니다.

        return result;  // 6자리 인증 번호를 반환합니다.
    }
}
