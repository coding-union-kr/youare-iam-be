package com.letter.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "OAuth", description = "소셜 로그인 관련 API")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    /**
     * 카카오에서 access token 가져오기
     * 가져온 access token 으로 카카오 사용자 정보 조회
     * 닉네임과 이메일을 가져와서 dto에 담기
     * 디비에 회원 정보가 있을 경우 그냥 access token 발급, 없을 경우 디비에 회원 정보 저장 후 access token 발급하기
     *
     * @param code
     * @return
     */

    @Operation(summary = "jwt 토큰 발급 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 발급 성공")
    })
    @GetMapping("/kakao/callback")
    public ResponseEntity<String> kakaoCallback(@RequestParam(name = "code") String code) { // 쿼리 스트링으로 들어오니까 @RequestParam 붙이기
        System.out.println("성공적으로 카카오 로그인 API 인가 코드를 불러왔습니다." + "code는 " + code);
        return oAuthService.getOAuthInfo(code);
    }


    @Operation(summary = "Refresh token으로 새 Access token 발급 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 발급 성공")
    })
    @PostMapping("/auth/token")
    public ResponseEntity<Void> issuedAccessTokenByRefreshToken(HttpServletRequest httpServletRequest) {
        return oAuthService.issuedAccessTokenByRefreshToken(httpServletRequest);
    }


    @Operation(summary = "로그아웃 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "205", description = "로그아웃 성공")
    })
    @DeleteMapping("/auth/logout")
    public ResponseEntity<Void> logout(HttpServletRequest httpServletRequest) {
        oAuthService.logout(httpServletRequest);
        return ResponseEntity.status(HttpStatus.RESET_CONTENT).body(null);
    }
}
