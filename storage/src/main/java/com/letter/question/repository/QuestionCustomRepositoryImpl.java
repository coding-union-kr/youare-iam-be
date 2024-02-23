package com.letter.question.repository;

import com.letter.member.entity.Couple;
import com.letter.question.dto.LetterDetailResponse;
import com.letter.question.dto.QuestionContentsResponse;
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

    // TODO SelectQuestion에서 조회하는 메서드를 SelectQuestionCustomRepositoryImpl 클래스로 이동
    public List<LetterDetailResponse> findAllByCoupleAndNextCursor(Couple couple, int nextCursor) {
        if (nextCursor == 0) {
            return jpaQueryFactory
                    .select(Projections.bean(LetterDetailResponse.class,
                            selectQuestion.id.as("selectQuestionId"),
                            question.questionContents.as(QUESTION),
                            selectQuestion.createdAt))
                    .from(selectQuestion)
                    .leftJoin(question)
                    .on(selectQuestion.question.id.eq(question.id),
                            question.isShow.eq("Y"))
                    .where(selectQuestion.couple.eq(couple),
                            selectQuestion.isShow.eq("Y"))
                    .orderBy(selectQuestion.id.desc())
                    .limit(26)
                    .fetch();
        } else {
            return jpaQueryFactory
                    .select(Projections.bean(LetterDetailResponse.class,
                            selectQuestion.id.as("selectQuestionId"),
                            question.questionContents.as(QUESTION),
                            selectQuestion.createdAt))
                    .from(selectQuestion)
                    .leftJoin(question)
                    .on(selectQuestion.question.id.eq(question.id),
                            question.isShow.eq("Y"))
                    .where(selectQuestion.id.loe(nextCursor),
                            selectQuestion.couple.eq(couple),
                            selectQuestion.isShow.eq("Y"))
                    .orderBy(selectQuestion.id.desc())
                    .limit(26)
                    .fetch();
        }
    }

    public QuestionContentsResponse findQuestionContentsBySelectQuestionIdAndCouple(Long selectQuestionId, Couple couple) {
        return jpaQueryFactory
                .select(Projections.bean(QuestionContentsResponse.class,
                        question.questionContents.as(QUESTION)))
                .from(selectQuestion)
                .leftJoin(question)
                .on(selectQuestion.question.eq(question),
                        question.isShow.eq("Y"))
                .where(selectQuestion.id.eq(selectQuestionId),
                        selectQuestion.couple.eq(couple),
                        selectQuestion.isShow.eq("Y"))
                .fetchOne();
    }

}
