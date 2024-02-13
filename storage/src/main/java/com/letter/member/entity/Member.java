package com.letter.member.entity;

import com.letter.member.dto.OAuthResponse;
import com.letter.question.entity.Answer;
import com.letter.question.entity.RegisterQuestion;
import com.letter.security.CryptoStringConverter;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Hidden
@Getter
@Setter
@Entity
@Table(name = "YI_MEMBER", schema = "YI")
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @Size(max = 30)
    @Column(name = "MBR_ID", nullable = false, length = 30)
    private String id;

    @Min(value = 0)
    @Column(name = "KAKAO_ID", nullable = false)
    private Long kakaoId;

    @Size(max = 100)
    @Column(name = "REFRESH_TOKEN", length = 100)
    private String refreshToken;

    @Size(max = 50)
    @NotNull
    @Column(name = "MEDIA_SE", nullable = false, length = 50)
    private String mediaSeparator;

    @Size(max = 50)
    @NotNull
    @Convert(converter = CryptoStringConverter.class)
    @Column(name = "MBR_NM", nullable = false, length = 50)
    private String name;

    @Size(max = 200)
    @NotNull
    @Convert(converter = CryptoStringConverter.class)
    @Column(name = "MBR_EML", nullable = false, length = 200)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CP_ID")
    private Couple couple;

    @CreatedDate
    @NotNull
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "MODIFIED_AT")
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<InviteOpponent> inviteOpponents = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<RegisterQuestion> registerQuestions = new ArrayList<>();

    /**
     * 디비에 저장 할 값 셋팅
     * @param userInfo
     * @param memberCount
     */
    public void saveUserInfo(OAuthResponse userInfo, Long memberCount, String refreshToken) {
        // 생성한 회원 아이디
        createMemberId(memberCount);
        // 값 셋팅
        this.name = userInfo.getNickname();
        this.email = userInfo.getEmail();
        this.kakaoId = userInfo.getId();
        this.refreshToken = refreshToken;

        // TODO 임시 값 변경
        this.mediaSeparator = "kakao";
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void resetRefreshToken() {
        this.refreshToken = null;
    }


    /**
     * 회원 아이디 생성(등록날짜+총 회원 수 마지막 + 1, 총 13자리)
     * @param memberCount
     */
    private void createMemberId(Long memberCount) {
        // 등록 일시 포맷 변경(예:20231212)
        String prefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 회원 아이디 생성
        this.id = prefix.concat(String.format("%05d", memberCount));
    }

}
