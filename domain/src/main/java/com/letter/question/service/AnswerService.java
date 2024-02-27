package com.letter.question.service;

import com.letter.exception.CustomException;
import com.letter.exception.ErrorCode;
import com.letter.member.entity.Couple;
import com.letter.member.entity.Member;
import com.letter.member.repository.CoupleCustomRepositoryImpl;
import com.letter.question.dto.AnswerContentsResponse;
import com.letter.question.dto.AnswerRequest;
import com.letter.question.dto.ModifyAnswerRequest;
import com.letter.question.dto.QuestionContentsResponse;
import com.letter.question.entity.Answer;
import com.letter.question.entity.SelectQuestion;
import com.letter.question.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final CoupleCustomRepositoryImpl coupleCustomRepository;

    private final AnswerRepository answerRepository;
    private final AnswerCustomRepositoryImpl answerCustomRepository;
    private final SelectQuestionRepository selectQuestionRepository;
    private final SelectQuestionCustomRepositoryImpl selectQuestionCustomRepository;
    private final QuestionCustomRepositoryImpl questionCustomRepository;

    public ResponseEntity<QuestionContentsResponse> getAnswersQuestion(Long selectedQuestionId, Member member) {

        Optional<Couple> optionalCouple = coupleCustomRepository.findCoupleInMemberByMemberId(member.getId());
        if (optionalCouple.isEmpty()) {
            throw new CustomException(ErrorCode.COUPLE_NOT_FOUND);
        }

        final Couple couple = optionalCouple.get();

        final QuestionContentsResponse questionContentsResponse = questionCustomRepository.findQuestionContentsBySelectQuestionIdAndCouple(selectedQuestionId, couple);

        if (questionContentsResponse == null) {
            throw new CustomException(ErrorCode.SELECT_QUESTION_NOT_FOUND);
        }

        return ResponseEntity.ok(questionContentsResponse);
    }

    public ResponseEntity<?> registerAnswer(AnswerRequest answerRequest, Member member) {

        Optional<Couple> optionalCouple = coupleCustomRepository.findCoupleInMemberByMemberId(member.getId());
        if (optionalCouple.isEmpty()) {
            throw new CustomException(ErrorCode.COUPLE_NOT_FOUND);
        }

        final Couple couple = optionalCouple.get();

        final SelectQuestion selectQuestion = selectQuestionRepository.findByIdAndCouple(answerRequest.getSelectQuestionId(), couple).orElseThrow(
                () -> new CustomException(ErrorCode.SELECT_QUESTION_NOT_FOUND)
        );

        final int count = answerRepository.countByMemberAndSelectQuestion(member, selectQuestion);

        if (count == 1) {
            throw new CustomException(ErrorCode.ALREADY_ANSWER);
        }

        answerRepository.save(new Answer(couple, member, selectQuestion, answerRequest.getAnswer()));

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    public AnswerContentsResponse getAnswer(Long selectedQuestionId, Member member) {

        Optional<Couple> optionalCouple = coupleCustomRepository.findCoupleInMemberByMemberId(member.getId());
        if (optionalCouple.isEmpty()) {
            throw new CustomException(ErrorCode.COUPLE_NOT_FOUND);
        }

        final Couple couple = optionalCouple.get();

        final Long countedSelectQuestionBySelectQuestionId = selectQuestionCustomRepository.countSelectQuestionBySelectQuestionId(selectedQuestionId, couple);
        if (countedSelectQuestionBySelectQuestionId == null) {
            throw new CustomException(ErrorCode.SELECT_QUESTION_NOT_FOUND);
        }

        final String answerContents = answerCustomRepository.findAnswerContentsBySelectQuestionId(selectedQuestionId, member);
        if (answerContents == null) {
            throw new CustomException(ErrorCode.ANSWER_NOT_FOUND);
        }

        return new AnswerContentsResponse(answerContents);
    }

    @Transactional
    public void modifyAnswer(ModifyAnswerRequest modifyAnswerRequest, Member member) {

        Optional<Couple> optionalCouple = coupleCustomRepository.findCoupleInMemberByMemberId(member.getId());
        if (optionalCouple.isEmpty()) {
            throw new CustomException(ErrorCode.COUPLE_NOT_FOUND);
        }

        final Couple couple = optionalCouple.get();

        final SelectQuestion selectQuestion = selectQuestionCustomRepository.findAnswerBySelectQuestion(modifyAnswerRequest.getSelectQuestionId(), couple).orElseThrow(
                () -> new CustomException(ErrorCode.BAD_REQUEST)
        );

        final Answer answer = answerRepository.findAnswerBySelectQuestionAndMemberAndIsShow(selectQuestion, member, "Y").orElseThrow(
                () -> new CustomException(ErrorCode.BAD_REQUEST)
        );

        answer.updateAnswerContents(modifyAnswerRequest.getAnswer());
    }
}
