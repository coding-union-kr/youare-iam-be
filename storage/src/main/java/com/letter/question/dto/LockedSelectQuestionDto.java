package com.letter.question.dto;

import com.letter.question.entity.SelectQuestion;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LockedSelectQuestionDto {
    private SelectQuestion selectQuestion;
    private Long answerCount;
}
