package com.letter.question.entity;

import com.letter.member.entity.Couple;
import com.letter.member.entity.Member;
import com.letter.security.CryptoStringConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "YI_ANSWER", schema = "YI")
@DynamicInsert
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ANS_ID", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CP_ID", nullable = false)
    private Couple couple;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBR_ID")
    private Member member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SEL_QSTN_ID", nullable = false)
    private SelectQuestion selectQuestion;

    @Size(max = 1000)
    @NotNull
    @Convert(converter = CryptoStringConverter.class)
    @Column(name = "ANS", nullable = false, length = 1000)
    private String answerContents;

    @Size(max = 1)
    @ColumnDefault("Y")
    @Column(name = "IS_SHOW", length = 1)
    private String isShow;

    @NotNull
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "MODIFIED_AT")
    private LocalDateTime modifiedAt;

    public Answer(Couple couple, Member member, SelectQuestion selectQuestion, String answerContents) {
        this.couple = couple;
        this.member = member;
        this.selectQuestion = selectQuestion;
        this.answerContents = answerContents;
        this.createdAt = LocalDateTime.now();
    }

    public void updateAnswerContents(String answerContents) {
        this.answerContents = answerContents;
    }
}
