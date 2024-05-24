package com.belleange.mall.repository;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Repository  // 이 클래스가 데이터 접근 객체임을 나타냅니다.
@RequiredArgsConstructor  // 이 클래스의 생성자를 자동으로 생성하여 final 필드를 초기화합니다.
public class CertificationNumberRepository {

    private final StringRedisTemplate redisTemplate;  // Redis와 상호작용하기 위한 템플릿 객체

    private static final int EMAIL_VERIFICATION_LIMIT_IN_SECONDS = 300; // 인증 번호의 유효 기간(예: 300초)

    // 인증 번호를 Redis에 저장하는 메서드
    public void saveCertificationNumber(String email, String certificationNumber) {
        redisTemplate.opsForValue()
                .set(email, certificationNumber, Duration.ofSeconds(EMAIL_VERIFICATION_LIMIT_IN_SECONDS));
    }

    // Redis에서 인증 번호를 조회하는 메서드
    public String getCertificationNumber(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    // Redis에서 인증 번호를 삭제하는 메서드
    public void removeCertificationNumber(String email) {
        redisTemplate.delete(email);
    }

    // Redis에 인증 번호가 존재하는지 확인하는 메서드
    public boolean hasKey(String email) {
        Boolean keyExists = redisTemplate.hasKey(email);
        return keyExists != null && keyExists;
    }
}
