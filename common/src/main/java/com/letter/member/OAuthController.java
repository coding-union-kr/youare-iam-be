package com.letter.member;

import com.letter.member.dto.OAuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "OAuth", description = "소셜 로그인 관련 API")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class OAuthController {

    private final MemberService memberService;
    private final OAuthService oAuthService;

    /**
     * 카카오에서 access token 가져오기
     * 가져온 access token 으로 카카오 사용자 정보 조회
     * 닉네임과 이메일을 가져와서 dto에 담기
     * 디비에 회원 정보가 있을 경우 그냥 access token 발급, 없을 경우 디비에 회원 정보 저장 후 access token 발급하기
     * @param code
     * @return
     */

    @Operation(summary = "jwt 토큰 발급 API")
    @ApiResponses(value ={
            @ApiResponse(responseCode= "200",description = "토큰 발급 성공")
    })
    @PostMapping("/kakao/callback")
    public ResponseEntity<String> kakaoCallback(@RequestParam(name = "code") String code){ // 쿼리 스트링으로 들어오니까 @RequestParam 붙이기
        System.out.println("성공적으로 카카오 로그인 API 인가 코드를 불러왔습니다." + "code는 " + code);
        return oAuthService.getOAuthInfo(code);
    }

}
