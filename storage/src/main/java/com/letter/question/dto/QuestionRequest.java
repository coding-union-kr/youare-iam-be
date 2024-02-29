package com.letter.question.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionRequest {

    private Long questionId;

    @Size(max = 200)
    private String question;

}
