package com.example.demo.application;

import com.example.demo.dto.JobPostingDetailRes;
import com.example.demo.dto.JobPostingReq;
import com.example.demo.dto.JobPostingRes;
import com.example.demo.dto.JobPostingUpdateReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.demo.global.exception.BadRequestException;

public interface JobPostingService {

    /**
     * 새로운 채용공고를 생성합니다.
     * @param jobPostingReq 생성할 채용공고의 정보를 담고 있습니다. 반드시 유효한 회사 ID를 포함해야 합니다.
     * @return 생성된 채용공고의 ID
     * @throws BadRequestException 유효하지 않은 회사 ID인 경우 발생합니다. (INVALID_COMPANY_ID)
     */
    Long createJobPosting(JobPostingReq jobPostingReq);

    /**
     * 기존 채용공고를 수정합니다.
     * @param jobId 수정할 채용공고의 ID
     * @param jobPostingUpdateReq 수정할 채용공고의 새로운 정보를 담고 있습니다.
     * @throws BadRequestException 유효하지 않은 채용공고 ID인 경우 발생합니다. (INVALID_JOBPOSTING_ID)
     */
    void updateJobPosting(Long jobId, JobPostingUpdateReq jobPostingUpdateReq);

    /**
     * 채용공고를 삭제합니다.
     * @param jobId 삭제할 채용공고의 ID
     * @throws BadRequestException 유효하지 않은 채용공고 ID인 경우 발생합니다. (INVALID_JOBPOSTING_ID)
     */
    void deleteJobPosting(Long jobId);

    /**
     * 모든 채용공고를 검색하고 페이징 처리하여 반환합니다.
     * @param search 검색어 (선택적)
     * @param pageable 페이징 정보
     * @return 검색 결과와 페이징 정보를 포함한 Page 객체
     */
    Page<JobPostingRes> getAllJobPostings(String search, Pageable pageable);

    /**
     * 특정 채용공고의 상세 정보를 조회합니다.
     * @param jobId 조회할 채용공고의 ID
     * @return 채용공고의 상세 정보
     * @throws BadRequestException 유효하지 않은 채용공고 ID인 경우 발생합니다. (INVALID_JOBPOSTING_ID)
     */
    JobPostingDetailRes getJobPostingDetail(Long jobId);
}