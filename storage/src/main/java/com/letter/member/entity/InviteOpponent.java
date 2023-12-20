package com.letter.member.entity;

import com.letter.question.entity.Question;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "YI_INVITE_OPPONENT", schema = "YI")
public class InviteOpponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVT_ID", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBR_ID", nullable = false)
    private Member member;

    @NotNull
    @ManyToOne( optional = false)
    @JoinColumn(name = "QSTN_ID", nullable = false)
    private Question question;

    @Size(max = 200)
    @NotNull
    @Column(name = "LINK_KEY", nullable = false, length = 200)
    private String linkKey;

    @NotNull
    @Column(name = "ANS", nullable = false, length = 1000)
    private String answer;

    @CreatedDate
    @NotNull
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

}
