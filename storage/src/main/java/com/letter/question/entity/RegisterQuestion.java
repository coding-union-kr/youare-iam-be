package com.letter.question.entity;

import com.letter.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "YI_REGISTER_QUESTION", schema = "YI")
@DynamicInsert
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RegisterQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REG_QSTN_ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MBR_ID")
    private Member member;

    @Size(max = 200)
    @NotNull
    @Column(name = "QSTN", nullable = false, length = 200)
    private String question;

    @Size(max = 1)
    @ColumnDefault("Y")
    @Column(name = "IS_SHOW", length = 1)
    private String isShow;

    @CreatedDate
    @NotNull
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "MODIFIED_AT")
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "registerQuestion", fetch = FetchType.LAZY)
    private List<SelectQuestion> selectQuestions = new ArrayList<>();


    public RegisterQuestion(Member member, String question) {
        this.member = member;
        this.question = question;
    }
}