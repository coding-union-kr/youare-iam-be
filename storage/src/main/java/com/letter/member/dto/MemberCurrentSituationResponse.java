package com.letter.member.dto;

import com.letter.question.dto.WaitingAnswerSelectQuestionDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MemberCurrentSituationResponse {
    private final String memberId;
    private final List<WaitingAnswerSelectQuestionDto> waitingAnswerList;
    private final long periodOfUse;
    private final long perfectAnswer;
}
