package com.letter.question.repository;

import com.letter.question.entity.RegisterQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterQuestionRepository extends JpaRepository<RegisterQuestion, Long> {
}
