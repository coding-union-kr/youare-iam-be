package com.letter.question.service;

import com.letter.exception.CustomException;
import com.letter.exception.ErrorCode;
import com.letter.member.entity.Couple;
import com.letter.member.entity.Member;
import com.letter.member.repository.CoupleCustomRepositoryImpl;
import com.letter.question.dto.*;
import com.letter.question.entity.Question;
import com.letter.question.entity.SelectQuestion;
import com.letter.question.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final CoupleCustomRepositoryImpl coupleCustomRepository;

    private final QuestionRepository questionRepository;
    private final QuestionCustomRepositoryImpl questionCustomRepository;
    private final SelectQuestionRepository selectQuestionRepository;
    private final SelectQuestionCustomRepositoryImpl selectQuestionCustomRepository;
    private final AnswerCustomRepositoryImpl answerCustomRepository;

    public ResponseEntity<List<QuestionResponse.QuestionList>> getQuestionList(Member member) {
        final List<QuestionResponse.QuestionList> questionLists;

        Optional<Couple> optionalCouple = coupleCustomRepository.findCoupleInMemberByMemberId(member.getId());
        if (optionalCouple.isEmpty()) {
            questionLists = questionCustomRepository.findAll();
        } else {
            questionLists = questionCustomRepository.findAllByCouple(optionalCouple.get());
        }

        return ResponseEntity.ok(questionLists);
    }

    public ResponseEntity<QuestionResponse.SelectedQuestion> selectOrRegisterQuestion(
            QuestionRequest.SelectOrRegisterQuestion selectOrRegisterQuestion,
            Member member) {
        Long selectedQuestion = null;

        // 질문 프리셋에 있는 질문 등록
        if (selectOrRegisterQuestion.getQuestionId() != null) {

            // 질문 프리셋에 없는 질문일 경우
            final Question question = questionRepository.findQuestionById(selectOrRegisterQuestion.getQuestionId()).orElseThrow(
                    () -> new CustomException(ErrorCode.QUESTION_NOT_FOUND)
            );

            Optional<Couple> optionalCouple = coupleCustomRepository.findCoupleInMemberByMemberId(member.getId());
            if (optionalCouple.isEmpty()) {
                throw new CustomException(ErrorCode.COUPLE_NOT_FOUND);
            }

            final Couple couple = optionalCouple.get();

            // 1일 1질문
            final Long countByAlreadyRegisterQuestion = selectQuestionCustomRepository.countByAlreadyRegisterQuestion(couple);
            if (countByAlreadyRegisterQuestion == 1) {
                throw new CustomException(ErrorCode.CAN_NOT_REGISTER_QUESTION_TODAY);
            }

            final int countSelectQuestion = selectQuestionRepository.countByQuestionAndCouple(question, couple);
            if (countSelectQuestion == 1) {
                throw new CustomException(ErrorCode.ALREADY_SELECTED_QUESTION);
            }

            // 정상 플로우, 질문 프리셋에서 질문을 고른 경우
            selectedQuestion = saveQuestion(question, couple);

        } else {
            // TODO 커스텀 질문 등록
            log.info("커스텀 질문 등록");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new QuestionResponse.SelectedQuestion(selectedQuestion));
    }

    public Long saveQuestion(Question question, Couple couple) {

        final SelectQuestion selectQuestion = SelectQuestion.builder()
                .question(question)
                .couple(couple)
                .build();
        selectQuestionRepository.save(selectQuestion);

        return selectQuestion.getId();
    }

    public ResponseEntity<LetterPaginationDto> getLetterList(int nextCursor, Member member) {

        Optional<Couple> optionalCouple = coupleCustomRepository.findCoupleInMemberByMemberId(member.getId());
        if (optionalCouple.isEmpty()) {
            throw new CustomException(ErrorCode.COUPLE_NOT_FOUND);
        }

        final Couple couple = optionalCouple.get();

        final LetterPaginationDto letterPaginationDto = new LetterPaginationDto();

        final List<LetterDetailResponse> letterDetailResponses = questionCustomRepository.findAllByCoupleAndNextCursor(couple, nextCursor);

        if (letterDetailResponses.isEmpty()) {
            return ResponseEntity.ok(null);
        }

        letterPaginationDto.setMyId(member.getId());

        if (letterDetailResponses.size() == 26) {
            final LetterDetailResponse nextLetter = letterDetailResponses.get(letterDetailResponses.size() - 1);
            letterPaginationDto.setNextCursor(Math.toIntExact(nextLetter.getSelectQuestionId()));
            letterDetailResponses.remove(letterDetailResponses.size() - 1);
        }

        final List<DetailAnswerDto> databaseDetailAnswerDtoList = answerCustomRepository.findAllBySelectQuestionIdAndCouple(letterDetailResponses.get(0).getSelectQuestionId(), couple);
        Collections.reverse(letterDetailResponses);

        final HashMap<Long, List<DetailAnswerDto>> letterDetailHashmap = new HashMap<>();

        categorizeAnswer(databaseDetailAnswerDtoList, letterDetailHashmap);
        getAnswerList(letterDetailResponses, letterDetailHashmap, member);

        letterPaginationDto.setLetters(letterDetailResponses);
        return ResponseEntity.ok(letterPaginationDto);
    }

    private void categorizeAnswer(
            List<DetailAnswerDto> databaseDetailAnswerDtoList, HashMap<Long,
            List<DetailAnswerDto>> letterDetailHashmap
    ) {
        for (DetailAnswerDto detailAnswerDto : databaseDetailAnswerDtoList) {
            if (!letterDetailHashmap.containsKey(detailAnswerDto.getSelectQuestionId())) {
                letterDetailHashmap.put(detailAnswerDto.getSelectQuestionId(), new ArrayList<>());
                letterDetailHashmap.get(detailAnswerDto.getSelectQuestionId()).add(detailAnswerDto);
            } else {
                letterDetailHashmap.get(detailAnswerDto.getSelectQuestionId()).add(detailAnswerDto);
            }
        }
    }

    private void getAnswerList(
            List<LetterDetailResponse> letterDetailResponses,
            HashMap<Long, List<DetailAnswerDto>> letterDetailHashmap,
            Member member
    ) {
        for (LetterDetailResponse letterDetailResponse : letterDetailResponses) {
            if (letterDetailHashmap.containsKey(letterDetailResponse.getSelectQuestionId()) && !letterDetailHashmap.get(letterDetailResponse.getSelectQuestionId()).isEmpty()) {
                final List<DetailAnswerDto> detailAnswerList = letterDetailHashmap.get(letterDetailResponse.getSelectQuestionId());
                letterDetailResponse.setAnswerCount(detailAnswerList.size());

                if (detailAnswerList.size() == 1 && Objects.equals(detailAnswerList.get(0).getMemberId(), member.getId())) {
                    letterDetailResponse.setMyAnswer(true);
                } else if (detailAnswerList.size() == 2) {
                    letterDetailResponse.setMyAnswer(true);
                    final DetailAnswerDto detailAnswerDto1 = detailAnswerList.get(0);
                    final DetailAnswerDto detailAnswerDto2 = detailAnswerList.get(1);

                    letterDetailResponse.setAnswer(new ArrayList<>());
                    letterDetailResponse.getAnswer().add(new AnswerResponse(detailAnswerDto1));
                    letterDetailResponse.getAnswer().add(new AnswerResponse(detailAnswerDto2));
                }
            }
        }
    }

    @Transactional
    public void updateLockedSelectedQuestions() {

        final List<LockedSelectQuestionDto> lockedSelectQuestionDtoList = selectQuestionCustomRepository.findSelectQuestionByAnswerCount();

        for (LockedSelectQuestionDto lockedSelectQuestionDto : lockedSelectQuestionDtoList) {
            lockedSelectQuestionDto.getSelectQuestion().updateIsShow();
        }

    }

}
