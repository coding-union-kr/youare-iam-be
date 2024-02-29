package com.letter.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Schema(description = "답변 요청 DTO")
@Getter
public class AnswerRequest {
    @Schema(description = "답변의 질문 ID")
    @NotNull
    private Long selectQuestionId;

    @Schema(description = "답변 내용")
    @NotNull
    @Size(max = 1000)
    private String answer;

}
