package com.letter.member.repository;

import com.letter.member.entity.Couple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.letter.member.entity.QCouple.couple;
import static com.letter.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class CoupleCustomRepositoryImpl implements CoupleCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<Couple> findCoupleInMemberByMemberId(String memberId) {
        return Optional.ofNullable(jpaQueryFactory
                .select(couple)
                .from(member)
                .join(couple)
                .on(member.couple.eq(couple))
                .where(member.id.eq(memberId),
                        couple.isShow.eq("Y"))
                .fetchOne());
    }

    public LocalDateTime findStartedDateByCoupleId(Long coupleId) {
        return jpaQueryFactory
                .select(couple.startedAt)
                .from(couple)
                .where(couple.id.eq(coupleId)
                        .and(couple.isShow.eq("Y")))
                .fetchOne();
    }

}
