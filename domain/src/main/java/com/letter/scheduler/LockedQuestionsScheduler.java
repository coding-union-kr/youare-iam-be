package com.letter.scheduler;

import com.letter.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LockedQuestionsScheduler {

    private final QuestionService questionService;

    @Scheduled(cron = "0 0 0 * * *")
    public void autoUpdate() {
        questionService.updateLockedSelectedQuestions();

        log.info("일주일 지난 답변 2개 미만의 Select Question 비공개 처리 완료");
    }
}
