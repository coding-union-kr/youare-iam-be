package com.letter.question.repository;

import com.letter.question.entity.Question;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionCustomRepository {
    Optional<Question> findQuestionByIdAndIsShow(@NotNull Long id, String isShow);
}
