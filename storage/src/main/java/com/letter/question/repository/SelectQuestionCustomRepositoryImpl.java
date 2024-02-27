package com.letter.question.repository;

import com.letter.member.entity.Couple;
import com.letter.question.dto.LockedSelectQuestionDto;
import com.letter.question.entity.SelectQuestion;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.letter.question.entity.QSelectQuestion.selectQuestion;
import static com.letter.question.entity.QAnswer.answer;

@Repository
@RequiredArgsConstructor
public class SelectQuestionCustomRepositoryImpl implements SelectQuestionCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Long countByAlreadyRegisterQuestion(Couple couple) {
        return jpaQueryFactory
                .select(selectQuestion.count())
                .from(selectQuestion)
                .where(selectQuestion.couple.eq(couple),
                        selectQuestion.createdAt.after(LocalDate.now().atStartOfDay()),
                        selectQuestion.isShow.eq("Y"))
                .fetchFirst();
    }

    public List<LockedSelectQuestionDto> findSelectQuestionByAnswerCount() {
        // TODO 쿼리를 직접 작성해서 조회를 해본 결과 answer count는 없어도 원하는 결과를 조회할 수 있었음
        return jpaQueryFactory
                .select(Projections.bean(LockedSelectQuestionDto.class,
                        selectQuestion,
                        answer.count().as("answerCount")))
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
