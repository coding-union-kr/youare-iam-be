package com.letter.member.dto;

import com.letter.member.entity.Couple;
import com.letter.member.entity.InviteOpponent;
import com.letter.member.entity.Member;
import com.letter.question.entity.Answer;
import com.letter.question.entity.Question;
import com.letter.question.entity.SelectQuestion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

public class MemberRequest {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema(description = "상대 초대 링크 생성 Request")
    public static class CreateInviteLinkRequest{ // 초대 링크 생성 관련 request

        @Schema(description = "선택한 질문 ID")
        @NotNull
        private Long questionId;
        @Schema(description = "선택한 질문에 대한 답의")
        @NotNull
        @Size(max = 1000)
        private String answer;

        public InviteOpponent toCreateInviteLink(String uuid, Question question, Member member) {
            return InviteOpponent.builder()
                    .question(question)
                    .member(member)
                    .answer(answer)
                    .linkKey(uuid)
                    .isShow("Y")
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Schema(description = "초대 수락 request")
    public static class AcceptInviteLinkRequest{ // 초대 수락 관련 request

        @Schema(description = "받은 초대의 링크키")
        @NotNull
        @Size(max = 200)
        private String linkKey;
        @Schema(description = "초대 받은 질문에 대한 답변")
        @NotNull
        @Size(max = 1000)
        private String answer;

        public Couple toCoupleInfo() {
            return Couple.builder()
                    .isShow("Y")
                    .build();
        }

        public SelectQuestion toSelectQuestion(Couple couple,Question question) {
            return SelectQuestion.builder()
                    .question(question)
                    .couple(couple)
                    .isShow("Y")
                    .build();
        }

        public Answer toInvitedPersonAnswerInfo(Member member, SelectQuestion selectQuestion, String answer) {
            return Answer.builder()
                    .member(member)
                    .couple(selectQuestion.getCouple())
                    .selectQuestion(selectQuestion)
                    .answerContents(answer)
                    .isShow("Y")
                    .createdAt(LocalDateTime.now())
                    .build();
        }

        public Answer toInvitePersonAnswerInfo(InviteOpponent inviteOpponent, SelectQuestion selectQuestion) {
            return Answer.builder()
                    .member(inviteOpponent.getMember())
                    .couple(selectQuestion.getCouple())
                    .selectQuestion(selectQuestion)
                    .answerContents(inviteOpponent.getAnswer())
                    .isShow("Y")
                    .createdAt(inviteOpponent.getCreatedAt())
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class InvitedPersonInfoRequest{ // 초대된 링크로 랜딩되는 페이지 관련 request

        @NotNull
        @Size(max = 200)
        private String linkKey;

    }


}
