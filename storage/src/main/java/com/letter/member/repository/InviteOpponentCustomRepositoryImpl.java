package com.letter.member.repository;

import com.letter.member.entity.InviteOpponent;
import com.letter.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static com.letter.member.entity.QInviteOpponent.inviteOpponent;
import static com.letter.question.entity.QQuestion.question;

@Repository
@RequiredArgsConstructor
public class InviteOpponentCustomRepositoryImpl implements InviteOpponentCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    public String findSelectedQuestionIdByLinkKey(String linkKey) {

        return jpaQueryFactory
                .select(question.questionContents)
                .from(inviteOpponent)
                .leftJoin(question).on(inviteOpponent.question.id.eq(question.id))
                .where(inviteOpponent.linkKey.eq(linkKey))
                .fetchOne();
    }

    public Long updateIsShow(String memberId){
        Long updateIsShow = jpaQueryFactory
                .update(inviteOpponent)
                .set(inviteOpponent.isShow, "N")
                .where(inviteOpponent.member.id.eq(memberId))
                .execute();

        entityManager.flush();
        entityManager.clear();

        return updateIsShow;
    }

    /**
     * 상대 초대 테이블에 노출 여부가 "N" 존재여부 확인
     * @param memberId
     * @return
     */
    public Boolean existByMemberIdAndIsShow(String memberId){
        return jpaQueryFactory
                .selectFrom(inviteOpponent)
                .where(inviteOpponent.member.id.eq(memberId)
                        .and(inviteOpponent.isShow.eq("N")))
                .fetchFirst() != null;
    }

    /**
     * 회원 아이디와 등록일자로 데이터가 있는지 확인
     * @param memberId
     * @return
     */
    public InviteOpponent existByMemberIdAndCreatedAt(String memberId){

        LocalDateTime minutesAgo = LocalDateTime.now().minusDays(1);

        return jpaQueryFactory
                        .select(inviteOpponent)
                        .from(inviteOpponent)
                        .where(inviteOpponent.member.id.eq(memberId)
                                .and(inviteOpponent.createdAt.after(minutesAgo)))
                        .orderBy(inviteOpponent.createdAt.desc())
                        .fetchFirst();
    }

    /**
     * 링크 키와 등록일자로 해당 데이터가 있는지 확인
     * @param linkKey
     * @return
     */
    public InviteOpponent existByLinkKeyAndCreatedAt(String linkKey){

        LocalDateTime minutesAgo = LocalDateTime.now().minusDays(1);

        return jpaQueryFactory
                .select(inviteOpponent)
                .from(inviteOpponent)
                .where(inviteOpponent.linkKey.eq(linkKey)
                        .and(inviteOpponent.createdAt.after(minutesAgo)))
                .orderBy(inviteOpponent.createdAt.desc())
                .fetchFirst();
    }


    public String getLinkKey(Member member) {
        return jpaQueryFactory
                .select(inviteOpponent.linkKey)
                .from(inviteOpponent)
                .where(inviteOpponent.member.eq(member),
                        inviteOpponent.createdAt.after(LocalDateTime.now().minusDays(1)),
                        inviteOpponent.isShow.eq("Y"))
                .fetchOne();
    }
}
