package com.letter.question.repository;

import com.letter.member.entity.Couple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import static com.letter.question.entity.QSelectQuestion.selectQuestion;

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
}
