package com.letter.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "대화방 상세 정보 DTO")
@Getter
@Setter
public class LetterDetailDto {

    @Schema(description = "대화방 질문 ID")
    @NotNull
    private Long selectQuestionId;

    @Schema(description = "프리셋 질문 내용")
    private String question;

    @Schema(description = "프리셋 질문 내용")
    private String registerQuestion;

    @Schema(description = "질문 선택 날짜")
    @NotNull
    private LocalDateTime createdAt;

}
