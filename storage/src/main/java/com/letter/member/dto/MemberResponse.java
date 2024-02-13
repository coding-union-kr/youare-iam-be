package com.letter.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class MemberResponse {

    @RequiredArgsConstructor
    @Builder
    @AllArgsConstructor
    @Data
    @Schema(description = "상대 초대 링크 생성 response")
    public static class CreateInviteLinkResponse {
        @Schema(description = "생성한 링크키")
        private String linkKey;
        @Schema(description = "선택한 질문 내용")
        private String question;
        @Schema(description = "초대한 사람의 이름")
        private String invitedPersonName;
    }

    @RequiredArgsConstructor
    @Builder
    @AllArgsConstructor
    @Data
    @Schema(description = "초대 수락 response")
    public static class AcceptInviteLinkResponse {
        @Schema(description = "초대할 때 생성된 선택 질문 ID")
        private Long selectedQuestionId;
    }

    @RequiredArgsConstructor
    @Builder
    @AllArgsConstructor
    @Data
    @Schema(description = "초대 랜딩 페이지 관련 response")
    public static class InvitedPersonInfoResponse {
        @Schema(description = "선택한 질문 내용")
        private String question;
        @Schema(description = "초대한 사람의 이름")
        private String invitedPersonName;
    }

}
