package com.letter.member.dto;

import com.letter.member.dto.role.UserStatusRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "사용자 상태 response")
public class MemberStatusResponse {

    @Schema(description = "사용자 상태 role")
    private final UserStatusRole userStatus;
    @Schema(description = "커플이 아닌 사용자가 초대를 보낸 경우의 유효한 링크키")
    private final String linkKey;
}
