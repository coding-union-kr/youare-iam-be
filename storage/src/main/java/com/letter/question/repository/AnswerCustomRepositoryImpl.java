package com.letter.question.repository;

import static com.letter.question.entity.QAnswer.answer;

import com.letter.member.entity.Couple;
import com.letter.member.entity.Member;
import com.letter.question.dto.DetailAnswerDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AnswerCustomRepositoryImpl implements AnswerCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<DetailAnswerDto> findAllBySelectQuestionIdAndCouple(Long selectQuestionId, Couple couple) {
        return jpaQueryFactory
                .select(Projections.bean(DetailAnswerDto.class,
                        answer.selectQuestion.id.as("selectQuestionId"),
                        answer.member.id.as("memberId"),
                        answer.member.name.as("memberName"),
                        answer.answerContents.as("answer"),
                        answer.createdAt))
                .from(answer)
                .join(answer.member)
                .where(answer.couple.eq(couple),
                        answer.selectQuestion.id.lt(selectQuestionId + 1),
                        answer.isShow.eq("Y"))
                .orderBy(answer.selectQuestion.id.desc())
                .fetch();
    }

    public String findAnswerContentsBySelectQuestionId(Long selectQuestionId, Member member) {
        return jpaQueryFactory
                .select(answer.answerContents.as("answer"))
                .from(answer)
                .where(answer.selectQuestion.id.eq(selectQuestionId)
                        .and(answer.member.eq(member))
                        .and(answer.isShow.eq("Y")))
                .fetchOne();
    }

}
