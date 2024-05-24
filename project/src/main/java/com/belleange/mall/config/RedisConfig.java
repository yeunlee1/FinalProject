package com.belleange.mall.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration  // 이 클래스가 스프링의 설정 파일임을 나타냅니다.
public class RedisConfig {

    // application.properties 파일에서 값을 주입받습니다.
    @Value("${spring.data.redis.mail.host}")
    private String host;
    
    @Value("${spring.data.redis.mail.port}")
    private int port;

    @Bean  
    public RedisConnectionFactory redisMailConnectionFactory() {
        // LettuceConnectionFactory를 사용하여 RedisConnectionFactory를 생성합니다.
        // 호스트와 포트는 위에서 주입받은 값을 사용합니다.
        return new LettuceConnectionFactory(host, port);
    }

    @Bean  // 이 메서드도 스프링 컨텍스트에 의해 관리되는 빈을 생성함을 나타냅니다.
    public StringRedisTemplate redisTemplate() {
        // StringRedisTemplate 인스턴스를 생성합니다.
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        // 위에서 생성한 RedisConnectionFactory를 설정합니다.
        stringRedisTemplate.setConnectionFactory(redisMailConnectionFactory());
        // 생성한 StringRedisTemplate 빈을 반환합니다.
        return stringRedisTemplate;
    }
}
