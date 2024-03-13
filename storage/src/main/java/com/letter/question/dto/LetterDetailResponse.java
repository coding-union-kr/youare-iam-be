package com.letter.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "대화방 상세 정보 응답 DTO")
@Getter
@Setter
public class LetterDetailResponse {
    @Schema(description = "대화방 질문 ID")
    @NotNull
    private Long selectQuestionId;

    @Schema(description = "질문 내용")
    @NotNull
    private String question;

    @Schema(description = "질문 선택 날짜")
    @NotNull
    private LocalDateTime createdAt;

    @Schema(description = "질문에 달린 답변 수")
    private int answerCount;

    @Schema(description = "나의 답변 존재 여부")
    private boolean isMyAnswer;

    @Schema(description = "답변 리스트")
    private List<AnswerResponse> answer;

    public LetterDetailResponse(Long selectQuestionId, String question, LocalDateTime createdAt) {
        this.selectQuestionId = selectQuestionId;
        this.question = question;
        this.createdAt = createdAt;
    }
}
