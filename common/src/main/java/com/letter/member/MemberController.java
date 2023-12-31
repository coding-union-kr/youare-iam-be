package com.letter.member;

import com.letter.annotation.LoginCheck;
import com.letter.annotation.User;
import com.letter.member.dto.MemberRequest;
import com.letter.member.dto.MemberResponse;
import com.letter.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Invite", description = "초대 관련 API")
@RestController
@RequestMapping("/api/v1/members/invite")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "초대 링크 생성 API")
    @ApiResponses(value ={
            @ApiResponse(responseCode= "201",description = "초대 링크 키 생성 완료")
    })
    @LoginCheck
    @PostMapping("/link")
    public ResponseEntity<MemberResponse.CreateInviteLinkResponse> createInviteLink(@RequestBody @Valid MemberRequest.CreateInviteLinkRequest request, @User Member member){

        return ResponseEntity.ok().body(memberService.createInviteLink(request,member));
    }

    @Operation(summary = "초대 수락 API")
    @ApiResponses(value ={
            @ApiResponse(responseCode= "201",description = "초대 수락 완료")
    })
    @LoginCheck
    @PostMapping("/accept")
    public ResponseEntity<MemberResponse.AcceptInviteLinkResponse> acceptedInvite(@RequestBody @Valid MemberRequest.AcceptInviteLinkRequest request, @User Member member){

        return ResponseEntity.ok().body(memberService.acceptedInvite(request,member));
    }

    @Operation(summary = "초대 링크로 랜딩되는 페이지 API")
    @ApiResponses(value ={
            @ApiResponse(responseCode= "200",description = "정보 가져오기 완료")
    })
    @GetMapping ("/info/{linkKey}")
    public ResponseEntity<MemberResponse.InvitedPersonInfoResponse> getInvitedPersonInfo(@PathVariable("linkKey") String linkKey){

        return ResponseEntity.ok().body(memberService.getInvitedPersonInfo(linkKey));
    }
}
