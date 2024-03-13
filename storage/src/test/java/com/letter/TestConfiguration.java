package com.letter;

import com.letter.security.CryptoHelper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {

    // "No qualifying bean of type" 에러 발생으로 직접 Bean 등록
    @Bean
    public CryptoHelper cryptoHelper() {
        return new CryptoHelper();
    }
}
