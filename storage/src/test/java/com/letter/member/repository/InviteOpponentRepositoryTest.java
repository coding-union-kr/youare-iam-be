package com.letter.member.repository;

import com.letter.TestConfiguration;
import com.letter.config.TestQuerydslConfig;
import com.letter.member.dto.OAuthResponse;
import com.letter.member.entity.InviteOpponent;
import com.letter.member.entity.Member;
import com.letter.question.entity.Question;
import com.letter.question.repository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = {
        TestConfiguration.class,
        TestQuerydslConfig.class
})
@DataJpaTest(includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = Repository.class
        ) // querydsl 클래스 파일은 CRUDRepository 를 상속받지 않기 때문에 Repository 어노테이션을 추가해도 Component 라서 스캔이 되지 않음 그래서 추가한 설정
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InviteOpponentRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private InviteOpponentRepository inviteOpponentRepository;
    @Autowired
    private InviteOpponentCustomRepositoryImpl inviteOpponentCustomRepository;

    @Test
    @DisplayName("save() 후 DB 조회 테스트")
    void testSaveLinkKey() {
        // given
        final Question question = questionRepository.findQuestionById(1L).orElseThrow();

        final Member member = new Member();
        member.saveUserInfo(OAuthResponse.builder()
                        .email("test@test.com")
                        .id(123456789L)
                        .nickname("test")
                        .build(),
                555L,
                null);
        memberRepository.save(member);

        final String uuid = UUID.randomUUID().toString();

        final InviteOpponent inviteOpponent = InviteOpponent.builder()
                .question(question)
                .member(member)
                .answer("몰라")
                .linkKey(uuid)
                .build();

        // when
        inviteOpponentRepository.save(inviteOpponent);
        final String linkKey = getLinkKey(member);

        // then
        assertThat(linkKey).isEqualTo(uuid);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    String getLinkKey(Member member) {
        return inviteOpponentCustomRepository.getLinkKey(member);
    }

}
