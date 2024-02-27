package com.letter.question.repository;

import com.letter.member.entity.Member;
import com.letter.question.entity.Answer;
import com.letter.question.entity.SelectQuestion;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    int countByMemberAndSelectQuestion(Member member, SelectQuestion selectQuestion);

    Optional<Answer> findAnswerBySelectQuestionAndMemberAndIsShow(
            SelectQuestion selectQuestion,
            Member member,
            @Size(max = 1) String isShow
    );
}
