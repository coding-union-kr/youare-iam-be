package com.letter.question.repository;

import com.letter.member.entity.Couple;
import com.letter.question.entity.Question;
import com.letter.question.entity.SelectQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SelectQuestionRepository extends JpaRepository<SelectQuestion, Long> {

    int countByQuestionAndCouple(Question question, Couple couple);

    Optional<SelectQuestion> findByIdAndCoupleAndIsShow(Long id, Couple couple, String isShow);

}
