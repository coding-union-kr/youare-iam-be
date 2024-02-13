package com.letter.member;

import com.letter.annotation.LoginCheck;
import com.letter.annotation.User;
import com.letter.exception.ExceptionResponse;
import com.letter.member.dto.MemberRequest;
import com.letter.member.dto.MemberResponse;
import com.letter.member.dto.MemberStatusResponse;
import com.letter.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;


    @Operation(summary = "초대 링크 생성 API", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "초대 링크 키 생성 완료",
                    content = @Content(schema = @Schema(implementation = MemberResponse.CreateInviteLinkResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Q001, 프리셋에 존재하지 않는 질문",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "C002, 이미 커플이 된 회원",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "C004, 이미 초대링크가 생성",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @LoginCheck
    @PostMapping("/invite/link")
    // TODO RequestDTO에 검증 관련 어노테이션이 존재하지 않는데 @Valid를 사용한 이유
    public ResponseEntity<MemberResponse.CreateInviteLinkResponse> createInviteLink(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
            @RequestBody
            @Valid
            MemberRequest.CreateInviteLinkRequest request,
            @User
            Member member
    ) {
        // TODO 데이터 생성이기 때문에 POST 메서드 API로 설계. 응답 상태 코드가 201이 되어야 하는게 아닌지
        return ResponseEntity.ok().body(memberService.createInviteLink(request, member));
    }


    @Operation(summary = "초대 수락 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "초대 수락 완료",
                    content = @Content(schema = @Schema(implementation = MemberResponse.AcceptInviteLinkResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "B001, 유효하지 않은 링크키",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "M002, 회원을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "C002, 이미 커플인 회원",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "C003, 상대가 이미 커플인 회원",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @LoginCheck
    @PostMapping("/invite/accept")
    // TODO RequestDTO에 검증 관련 어노테이션이 존재하지 않는데 @Valid를 사용한 이유
    public ResponseEntity<MemberResponse.AcceptInviteLinkResponse> acceptedInvite(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true)
            @RequestBody
            @Valid
            MemberRequest.AcceptInviteLinkRequest request,
            @User
            Member member
    ) {
        // TODO 데이터 생성이기 때문에 POST 메서드 API로 설계. 응답 상태 코드가 201이 되어야 하는게 아닌지
        return ResponseEntity.ok().body(memberService.acceptedInvite(request, member));
    }


    @Operation(summary = "초대 링크로 랜딩되는 페이지 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "정보 가져오기 완료",
                    content = @Content(schema = @Schema(implementation = MemberResponse.InvitedPersonInfoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "B001, 유효하지 않은 링크키",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            )
    })
    @GetMapping("/invite/info/{linkKey}")
    public ResponseEntity<MemberResponse.InvitedPersonInfoResponse> getInvitedPersonInfo(
            @Parameter(required = true, description = "받은 초대의 링크키")
            @PathVariable("linkKey")
            String linkKey
    ) {
        return ResponseEntity.ok().body(memberService.getInvitedPersonInfo(linkKey));
    }


    @Operation(summary = "사용자의 상태 확인 API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상태 확인 완료",
                    content = @Content(schema = @Schema(implementation = MemberStatusResponse.class))
            )
    })
    @LoginCheck
    @GetMapping("/user-status")
    public ResponseEntity<MemberStatusResponse> getUserStatus(@User Member member) {
        return ResponseEntity.ok(memberService.getUserStatus(member));
    }

}
