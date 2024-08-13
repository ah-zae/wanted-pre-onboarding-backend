package com.example.demo.application;

import com.example.demo.dto.ApplicationReq;
import com.example.demo.global.exception.BadRequestException;

public interface ApplicationService {

    /**
     * 채용공고 지원
     * @param applicationReq 채용공고 지원 요청 정보를 담고 있습니다. 반드시 유효한 jobPostingId와 userId를 포함해야합니다.
     * @throws BadRequestException 다음의 경우에 발생합니다:
     *          - 유효하지 않은 채용공고 ID (INVALID_JOBPOSTING_ID)
     *          - 유효하지 않은 사용자 ID (INVALID_USER_ID)
     *          - 이미 해당 채용공고에 지원한 경우 (ALREADY_APPLY)
     */
    void applyToJob(ApplicationReq applicationReq);
}
