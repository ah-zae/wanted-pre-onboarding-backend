package com.example.demo.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    // ---------------------------- Company 1000 ~ 1999  ---------------------------------------
    INVALID_COMPANY_ID(1000, "유효하지 않은 회사입니다."),
    INVALID_JOBPOSTING_ID(1001, "잘못된 채용공고 ID입니다."),
    INVALID_USER_ID(1002,"잘못된 유저 ID입니다."),
    ALREADY_APPLY(1003,"이미 지원한 채용공고입니다."),



    // ---------------------------- Common Error 9500 ~ 9999  ---------------------------------------
    INTERNAL_SEVER_ERROR(9500,"서버 내부 오류입니다. 관리자에게 문의하세요"),
    INVALID_REQUEST(9501,"유효하지않은 요청입니다."),
    METHOD_NOT_ALLOWED(9502,"지원하지 않는 HTTP 메소드입니다.");

    private final int code;
    private final String message;
}
