package com.example.demo.application;

import com.example.demo.domain.Company;
import com.example.demo.domain.JobPosting;
import com.example.demo.domain.repository.CompanyRepository;
import com.example.demo.domain.repository.JobPostingRepository;
import com.example.demo.dto.JobPostingDetailRes;
import com.example.demo.dto.JobPostingReq;
import com.example.demo.dto.JobPostingRes;
import com.example.demo.dto.JobPostingUpdateReq;
import com.example.demo.global.exception.BadRequestException;
import com.example.demo.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.demo.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobPostingServiceImpl implements JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public Long createJobPosting(JobPostingReq jobPostingReq) {

        // 해당 회사가 존재하는지 확인
        Company company = companyRepository.findById(jobPostingReq.getCompanyId())
                .orElseThrow(() -> new BadRequestException(INVALID_COMPANY_ID));

        JobPosting jobPosting = JobPosting.builder()
                .company(company)
                .position(jobPostingReq.getPosition())
                .reward(jobPostingReq.getReward())
                .description(jobPostingReq.getDescription())
                .techStack(jobPostingReq.getTechStack())
                .build();

        jobPostingRepository.save(jobPosting);
        return jobPosting.getId();
    }

    @Override
    @Transactional
    public void updateJobPosting(Long jobPostingId, JobPostingUpdateReq jobPostingUpdateReq) {

        // 해당 채용공고가 있는지 확인
        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new BadRequestException(INVALID_JOBPOSTING_ID));

        jobPosting.setPosition(jobPostingUpdateReq.getPosition());
        jobPosting.setReward(jobPostingUpdateReq.getReward());
        jobPosting.setDescription(jobPostingUpdateReq.getDescription());
        jobPosting.setTechStack(jobPostingUpdateReq.getTechStack());

        // 수정된 채용공고 저장
        jobPostingRepository.save(jobPosting);
    }

    @Override
    @Transactional
    public void deleteJobPosting(Long jobPostingId) {

        // 해당 채용공고가 있는지 확인
        JobPosting jobPosting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new BadRequestException(INVALID_JOBPOSTING_ID));

        jobPostingRepository.deleteById(jobPostingId);
    }

    @Override
    public Page<JobPostingRes> getAllJobPostings(String search, Pageable pageable) {
        Page<JobPosting> pages = jobPostingRepository.findJobPostings(pageable, search);
        return pages.map((jobPosting) -> JobPostingRes.fromEntity(jobPosting,jobPosting.getCompany()));

    }
    @Override
    public JobPostingDetailRes getJobPostingDetail(Long jobId) {
        // ID로 채용공고 정보 조회
        JobPosting jobPosting = jobPostingRepository.findJobPostingJoinCompany(jobId)
                .orElseThrow(() -> new BadRequestException(INVALID_JOBPOSTING_ID));

        Company company = jobPosting.getCompany();
        List<Long> jobPostingIdList = new ArrayList<>();

        for(JobPosting otherJobPosition : company.getJobPostings()){
            if(!Objects.equals(otherJobPosition.getId(), jobPosting.getId())){
                jobPostingIdList.add(otherJobPosition.getId());
            }
        }


        // JobPostingRes로 변환하여 반환
        return JobPostingDetailRes.fromEntity(jobPosting,company,jobPostingIdList);
    }
}
