package com.letter.question.repository;

import com.letter.member.entity.Couple;
import com.letter.question.dto.LockedSelectQuestionDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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
}
