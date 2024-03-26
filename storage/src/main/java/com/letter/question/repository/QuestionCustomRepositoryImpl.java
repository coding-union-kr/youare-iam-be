package com.letter.question.repository;

import com.letter.member.entity.Couple;
import com.letter.question.dto.QuestionResponse;

import static com.letter.question.entity.QQuestion.question;
import static com.letter.question.entity.QSelectQuestion.selectQuestion;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionCustomRepositoryImpl implements QuestionCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static final String QUESTION = "question";

    public List<QuestionResponse.QuestionList> findAll() {
        return jpaQueryFactory
                .select(Projections.bean(QuestionResponse.QuestionList.class,
                        question.id.as("questionId"),
                        question.questionContents.as(QUESTION)))
                .from(question)
                .where(question.isShow.eq("Y"))
                .fetch();
    }

    // 질문 리스트 조회
    public List<QuestionResponse.QuestionList> findAllByCouple(Couple couple) {
        return jpaQueryFactory
                .select(Projections.bean(QuestionResponse.QuestionList.class,
                        question.id.as("questionId"),
                        question.questionContents.as(QUESTION)))
                .from(question)
                .leftJoin(selectQuestion)
                .on(selectQuestion.couple.eq(couple),
                        selectQuestion.question.eq(question))
                .where(selectQuestion.id.isNull(), question.isShow.eq("Y"))
                .fetch();
    }

}
