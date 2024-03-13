package com.letter.question.repository;

import com.letter.member.entity.Couple;
import com.letter.question.dto.LetterDetailDto;
import com.letter.question.dto.LockedSelectQuestionDto;
import com.letter.question.dto.QuestionContentsResponse;
import com.letter.question.entity.SelectQuestion;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.letter.question.entity.QQuestion.question;
import static com.letter.question.entity.QSelectQuestion.selectQuestion;
import static com.letter.question.entity.QRegisterQuestion.registerQuestion;
import static com.letter.question.entity.QAnswer.answer;

@Repository
@RequiredArgsConstructor
public class SelectQuestionCustomRepositoryImpl implements SelectQuestionCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static final String QUESTION = "question";

    public Long countByAlreadyRegisterQuestion(Couple couple) {
        return jpaQueryFactory
                .select(selectQuestion.count())
                .from(selectQuestion)
                .where(selectQuestion.couple.eq(couple),
                        selectQuestion.createdAt.after(LocalDate.now().atStartOfDay()),
                        selectQuestion.isShow.eq("Y"))
                .fetchFirst();
    }

    public List<LetterDetailDto> findAllByCoupleAndNextCursor(Couple couple, int nextCursor) {
        if (nextCursor == 0) {
            return jpaQueryFactory
                    .select(Projections.bean(LetterDetailDto.class,
                            selectQuestion.id.as("selectQuestionId"),
                            question.questionContents.as(QUESTION),
                            registerQuestion.question.as("registerQuestion"),
                            selectQuestion.createdAt))
                    .from(selectQuestion)
                    .leftJoin(question)
                    .on(selectQuestion.question.eq(question)
                            .and(question.isShow.eq("Y")))
                    .leftJoin(registerQuestion)
                    .on(selectQuestion.registerQuestion.eq(registerQuestion)
                            .and(registerQuestion.isShow.eq("Y")))
                    .where(selectQuestion.couple.eq(couple),
                            selectQuestion.isShow.eq("Y"))
                    .orderBy(selectQuestion.id.desc())
                    .limit(26)
                    .fetch();
        } else {
            return jpaQueryFactory
                    .select(Projections.bean(LetterDetailDto.class,
                            selectQuestion.id.as("selectQuestionId"),
                            question.questionContents.as(QUESTION),
                            registerQuestion.question.as("registerQuestion"),
                            selectQuestion.createdAt))
                    .from(selectQuestion)
                    .leftJoin(question)
                    .on(selectQuestion.question.id.eq(question.id)
                            .and(question.isShow.eq("Y")))
                    .leftJoin(registerQuestion)
                    .on(selectQuestion.registerQuestion.eq(registerQuestion)
                            .and(registerQuestion.isShow.eq("Y")))
                    .where(selectQuestion.id.loe(nextCursor),
                            selectQuestion.couple.eq(couple),
                            selectQuestion.isShow.eq("Y"))
                    .orderBy(selectQuestion.id.desc())
                    .limit(26)
                    .fetch();
        }
    }

    public QuestionContentsResponse findQuestionContentsBySelectQuestionIdAndCouple(boolean hasQuestion, Long selectQuestionId) {
        if (hasQuestion) {
            return jpaQueryFactory
                    .select(Projections.bean(QuestionContentsResponse.class,
                            question.questionContents.as(QUESTION)))
                    .from(question)
                    .leftJoin(selectQuestion)
                    .on(selectQuestion.id.eq(selectQuestionId)
                            .and(selectQuestion.isShow.eq("Y")))
                    .where(selectQuestion.question.eq(question)
                            .and(question.isShow.eq("Y")))
                    .fetchOne();
        } else {
            return jpaQueryFactory
                    .select(Projections.bean(QuestionContentsResponse.class,
                            registerQuestion.question))
                    .from(registerQuestion)
                    .leftJoin(selectQuestion)
                    .on(selectQuestion.id.eq(selectQuestionId)
                            .and(selectQuestion.isShow.eq("Y")))
                    .where(selectQuestion.registerQuestion.eq(registerQuestion)
                            .and(registerQuestion.isShow.eq("Y")))
                    .fetchOne();
        }
    }

    public List<LockedSelectQuestionDto> findSelectQuestionByAnswerCount() {
        return jpaQueryFactory
                .select(Projections.bean(LockedSelectQuestionDto.class,
                        selectQuestion))
                .from(selectQuestion)
                .leftJoin(answer)
                .on(answer.selectQuestion.eq(selectQuestion),
                        answer.isShow.eq("Y"))
                .where(selectQuestion.createdAt.before(LocalDate.now().minusDays(7).atStartOfDay()),
                        selectQuestion.isShow.eq("Y"))
                .groupBy(selectQuestion.id)
                .having(answer.selectQuestion.id.count().lt(2))
                .fetch();
    }

    public Long countSelectQuestionBySelectQuestionId(Long selectQuestionId, Couple couple) {
        return jpaQueryFactory
                .select(selectQuestion.count())
                .from(selectQuestion)
                .where(selectQuestion.id.eq(selectQuestionId)
                        .and(selectQuestion.couple.eq(couple))
                        .and(selectQuestion.isShow.eq("Y")))
                .fetchOne();
    }

    public Optional<SelectQuestion> findAnswerBySelectQuestion(Long selectQuestionId, Couple couple) {
        return Optional.ofNullable(jpaQueryFactory
                .select(selectQuestion)
                .from(selectQuestion)
                .leftJoin(answer)
                .on(answer.selectQuestion.id.eq(selectQuestionId)
                        .and(answer.couple.eq(couple))
                        .and(answer.isShow.eq("Y")))
                .where(selectQuestion.id.eq(selectQuestionId)
                        .and(selectQuestion.couple.eq(couple))
                        .and(selectQuestion.isShow.eq("Y")))
                .groupBy(selectQuestion.id)
                .having(answer.selectQuestion.id.count().lt(2))
                .fetchOne());
    }
}
