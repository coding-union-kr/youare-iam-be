package com.letter.question.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class WaitingAnswerSelectQuestionDto {
    private final long selectQuestionId;
    private final String question;
    private final LocalDateTime createdAt;
}
