package com.example.demo.application;

import com.example.demo.domain.Application;
import com.example.demo.domain.JobPosting;
import com.example.demo.domain.User;
import com.example.demo.domain.repository.ApplicationRepository;
import com.example.demo.domain.repository.JobPostingRepository;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.dto.ApplicationReq;
import com.example.demo.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void applyToJob(ApplicationReq applicationReq) {

        // 채용공고 존재 여부 확인
        JobPosting jobPosting = jobPostingRepository.findById(applicationReq.getJobPostingId())
                .orElseThrow(() -> new BadRequestException(INVALID_JOBPOSTING_ID));

        // 사용자 존재 여부 확인
        User user = userRepository.findById(applicationReq.getUserId())
                .orElseThrow(() -> new BadRequestException(INVALID_USER_ID));

        // 이미 지원한 지원자인지 확인
        boolean alreadyApplied = applicationRepository.existsByUserIdAndJobPostingId(applicationReq.getUserId(), applicationReq.getJobPostingId());
        if (alreadyApplied) {
            throw new BadRequestException(ALREADY_APPLY);
        }

        // 채용공고 지원
        Application application = Application.builder()
                .jobPosting(jobPosting)
                .user(user)
                .build();

        applicationRepository.save(application);
    }
}
