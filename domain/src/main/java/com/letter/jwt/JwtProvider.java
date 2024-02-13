package com.letter.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letter.exception.CustomException;
import com.letter.exception.ErrorCode;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JwtProvider {
    private static final Long REFRESH_TOKEN_VALID_TIME = Duration.ofDays(14).toMillis(); // 만료시간 2주

    @Value("${jwt.secret}")
    private String secretKey;

    public static final long EXPIRATION_TIME =  30 * 60 * 1000L; // 유효시간은 30분
    public static final String TOKEN_PREFIX = "Bearer "; // Bearer 뒤에 무조건 띄어쓰기 한번 해주기
    public static final String HEADER_STRING = "Authorization"; // 해더에 Authorization 이라는 토큰을 넣어 줌

    public static final String ACCESS_TYPE = "access";
    public static final String REFRESH_TYPE = "refresh";

    /**
     * jwt 토큰 생성하기
     * @param memberId
     * @param nickname
     * @return
     */
    public String createAccessToken(String memberId, String nickname) {

        return JWT.create()
                .withSubject(memberId) // Payload 에 들어갈 등록된 클레임 을 설정한다.
                .withExpiresAt(new Date(System.currentTimeMillis()+ EXPIRATION_TIME)) //JwtProperties 의 만료 시간 필드를 불러와 넣어준다.
                .withClaim("nickname", nickname) // Payload 에 들어갈 개인 클레임 을 설정한다.
                // .withClaim(이름, 내용) 형태로 작성한다. 사용자를 식별할 수 있는 값과, 따로 추가하고 싶은 값을 자유롭게 넣는다.
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * 헤더에서 토큰 가져오기
     * @param request
     * @return
     */
    public String bringToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_STRING);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7); // "Bearer " 을 제외한 토큰만 가져오기
        }
        return null;
    }

    /**
     * 토큰 검증
     * @param token
     * @param type
     */
    public void validateToken(String token, String type) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않는 JWT 서명 입니다.");
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {

            if (type.equals(ACCESS_TYPE)) {
                final ErrorCode expiredAccessToken = ErrorCode.EXPIRED_ACCESS_TOKEN;
                log.error("error message: {}", expiredAccessToken.getMessage());
                throw new CustomException(expiredAccessToken);
            }

            if (type.equals(REFRESH_TYPE)) {
                final ErrorCode expiredRefreshToken = ErrorCode.EXPIRED_REFRESH_TOKEN;
                log.error("error message: {}", expiredRefreshToken.getMessage());
                throw new CustomException(expiredRefreshToken);
            }

        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰 입니다.");
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰 입니다. 토큰이 비었을 수 있으니 다시 확인해주세요.");
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    /**
     * 토큰에서 사용자 정보 가져오기
     * @param token
     * @return
     */
    public String getUserInfoFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("sub", String.class);
    }



    /**
     * Refresh token 생성
     * @return Jwt Refresh token
     */
    public String createRefreshToken() {
        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME))
                .sign(Algorithm.HMAC512(secretKey));
    }


    public String expiredTokenCombo(String accessToken) {
        final String[] tokenParts = accessToken.split("\\.");
        validateExpiredTokenSignature(tokenParts);
        return getMemberIdByExpiredToken(tokenParts);
    }


    /**
     * 만료된 Access token signature 검사
     * token의 signature는 header + payload에 secretKey로 서명한 것이기 때문에 헤더 검사가 따로 필요하지 않다.
     * @param tokenParts
     */
    private void validateExpiredTokenSignature(String[] tokenParts) {
        final byte[] signatureBytes = Base64.getUrlDecoder().decode(tokenParts[2]);

        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            Mac hmacSHA512 = Mac.getInstance("HmacSHA512");
            hmacSHA512.init(secretKeySpec);

            String headerPayload = tokenParts[0] + "." + tokenParts[1];
            byte[] hashed = hmacSHA512.doFinal(headerPayload.getBytes(StandardCharsets.UTF_8));

            if (!MessageDigest.isEqual(hashed, signatureBytes)) {
                log.error("유효하지 않은 JWT 서명");
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }

        } catch (NoSuchAlgorithmException exception) {
            log.error("존재하지 않는 알고리즘");
        } catch (InvalidKeyException exception) {
            log.error("유효하지 않는 Secret key 명세");
        }
    }


    /**
     * 만료된 Access token 으로 claims 꺼내기
     * @param tokenParts
     * @return memberId
     */
    private String getMemberIdByExpiredToken(String[] tokenParts) {
        final String payloadJson = new String(Base64.getDecoder().decode(tokenParts[1]));

        Map<?, ?> claims = null;
        try {
            claims = new ObjectMapper().readValue(payloadJson, Map.class);
        } catch (JsonMappingException e) {
            log.error("Json 매핑 실패");
        } catch (JsonProcessingException exception) {
            log.error("Json 파싱 실패");
        }

        return (String) claims.get("sub");
    }

}
