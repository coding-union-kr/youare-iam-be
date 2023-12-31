package com.letter.question.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

public class QuestionResponse {

    @Getter
    @Setter
    public static class QuestionList {
        @NotNull
        private Long questionId;

        @NotNull
        @Size(max = 200)
        private String question;

    }

    @Getter
    @RequiredArgsConstructor
    public static class SelectedQuestion {
        @NotNull
        private final Long selectedQuestionId;
    }

}

