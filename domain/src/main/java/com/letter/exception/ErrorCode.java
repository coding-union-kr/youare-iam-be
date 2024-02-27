package com.letter.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 공통 Error */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "B001","잘못된 요청값 입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "B002","찾으시는 결과가 없습니다."),

    /* Auth 관련 Error */
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AU001","유효하지 않는 JWT 토큰 입니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AU002","만료된 Access token 입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AU003","만료된 Refresh token 입니다."),

    /* Member 관련 Error */
    MEMBER_BAD_REQUEST(HttpStatus.BAD_REQUEST, "M001","초대한 사람의 아이디와 초대된 사람의 아이디가 같습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M002","찾는 회원 아이디가 없습니다."),

    /* Couple 관련 Error */
    COUPLE_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "커플로 등록되지 않은 사용자입니다."),
    ALREADY_COUPLE(HttpStatus.CONFLICT, "C002", "이미 커플이 된 회원입니다."),
    OPPONENT_ALREADY_COUPLE(HttpStatus.CONFLICT, "C003", "상대는 이미 커플이 된 회원입니다."),
    ALREADY_LINK(HttpStatus.CONFLICT,"C004","이미 초대링크가 생성되었습니다."),
    EXPIRED_LINK(HttpStatus.CONFLICT,"C005","해당 링크는 유효시간이 만료되었습니다."),

    /* Question 관련 Error */
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Q001", "프리셋에 존재하지 않는 질문입니다."),
    SELECT_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Q002", "선택 질문을 찾을 수 없습니다."),
    ALREADY_SELECTED_QUESTION(HttpStatus.CONFLICT, "Q003", "이미 선택했던 질문입니다."),
    CAN_NOT_REGISTER_QUESTION_TODAY(HttpStatus.CONFLICT, "Q004", "오늘은 더 이상 질문을 등록할 수 없습니다."),

    /* Answer 관련 Error */
    ALREADY_ANSWER(HttpStatus.CONFLICT, "A001", "질문에 이미 답변을 작성했습니다."),
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "A002", "답변을 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

}
