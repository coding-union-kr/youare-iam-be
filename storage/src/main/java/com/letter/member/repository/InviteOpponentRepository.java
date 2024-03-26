package com.letter.member.repository;

import com.letter.member.entity.InviteOpponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface InviteOpponentRepository extends JpaRepository<InviteOpponent,Long> {

    Optional<InviteOpponent> findQuestionByLinkKeyAndIsShow(String linkKey, String isShow);
}
