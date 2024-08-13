package com.example.demo.application;

import com.example.demo.domain.Company;
import com.example.demo.domain.JobPosting;
import com.example.demo.domain.repository.CompanyRepository;
import com.example.demo.domain.repository.JobPostingRepository;
import com.example.demo.dto.*;
import com.example.demo.global.exception.BadRequestException;
import com.example.demo.global.exception.ExceptionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobPostingServiceImplTest {

    @Mock
    private JobPostingRepository jobPostingRepository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private JobPostingServiceImpl jobPostingService;

    private JobPostingReq validJobPostingReq;
    private JobPostingUpdateReq validJobPostingUpdateReq;
    private Company validCompany;
    private JobPosting validJobPosting;

    @BeforeEach
    void setUp() {
        validJobPostingReq = JobPostingReq.builder().build();
        ReflectionTestUtils.setField(validJobPostingReq, "companyId", 1L);
        ReflectionTestUtils.setField(validJobPostingReq, "position", "백엔드 주니어");
        ReflectionTestUtils.setField(validJobPostingReq, "reward", 100000);
        ReflectionTestUtils.setField(validJobPostingReq, "description", "원티드 백엔드 ");
        ReflectionTestUtils.setField(validJobPostingReq, "techStack", "Java,Spring");

        validJobPostingUpdateReq = JobPostingUpdateReq.builder().build();
        ReflectionTestUtils.setField(validJobPostingUpdateReq, "position", "백엔드 주니어2");
        ReflectionTestUtils.setField(validJobPostingUpdateReq, "reward", 150000);
        ReflectionTestUtils.setField(validJobPostingUpdateReq, "description", "원티드 백엔드2");
        ReflectionTestUtils.setField(validJobPostingUpdateReq, "techStack", "Java,Spring,React");

        validCompany = new Company();
        ReflectionTestUtils.setField(validCompany, "id", 1L);
        ReflectionTestUtils.setField(validCompany, "name", "네이버");
        ReflectionTestUtils.setField(validCompany, "country", "한국");
        ReflectionTestUtils.setField(validCompany, "region", "판교");
        ReflectionTestUtils.setField(validCompany, "jobPostings", new ArrayList<>());

        validJobPosting = new JobPosting();
        ReflectionTestUtils.setField(validJobPosting, "id", 1L);
        ReflectionTestUtils.setField(validJobPosting, "company", validCompany);
        ReflectionTestUtils.setField(validJobPosting, "position", "백엔드 주니어");
        ReflectionTestUtils.setField(validJobPosting, "reward", 100000);
        ReflectionTestUtils.setField(validJobPosting, "description", "원티드 백엔드");
        ReflectionTestUtils.setField(validJobPosting, "techStack", "Java,Spring");

        validCompany.getJobPostings().add(validJobPosting);
    }

    @DisplayName("유효한 요청으로 채용공고 생성 성공")
    @Test
    void createJobPosting_ValidRequest_Success() {
        // given
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(validCompany));
        when(jobPostingRepository.save(any(JobPosting.class))).thenReturn(validJobPosting);

        // when
        Long jobPostingId = jobPostingService.createJobPosting(validJobPostingReq);

        // then
        assertNull(jobPostingId); // createJobPosting 메서드가 현재 null을 반환하므로 이를 검증합니다.
        verify(companyRepository).findById(validJobPostingReq.getCompanyId());
        verify(jobPostingRepository).save(any(JobPosting.class));
    }

    @DisplayName("존재하지 않는 회사로 채용공고 생성 실패")
    @Test
    void createJobPosting_InvalidCompany_ThrowsException() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> jobPostingService.createJobPosting(validJobPostingReq));
        assertEquals(ExceptionCode.INVALID_COMPANY_ID.getCode(), exception.getCode());
        assertEquals(ExceptionCode.INVALID_COMPANY_ID.getMessage(), exception.getMessage());
    }

    @DisplayName("유효한 요청으로 채용공고 수정 성공")
    @Test
    void updateJobPosting_ValidRequest_Success() {
        when(jobPostingRepository.findById(anyLong())).thenReturn(Optional.of(validJobPosting));

        assertDoesNotThrow(() -> jobPostingService.updateJobPosting(1L, validJobPostingUpdateReq));

        verify(jobPostingRepository).save(any(JobPosting.class));
    }

    @DisplayName("존재하지 않는 채용공고 수정 실패")
    @Test
    void updateJobPosting_InvalidJobPosting_ThrowsException() {
        when(jobPostingRepository.findById(anyLong())).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> jobPostingService.updateJobPosting(1L, validJobPostingUpdateReq));
        assertEquals(ExceptionCode.INVALID_JOBPOSTING_ID.getCode(), exception.getCode());
        assertEquals(ExceptionCode.INVALID_JOBPOSTING_ID.getMessage(), exception.getMessage());
    }

    @DisplayName("유효한 요청으로 채용공고 삭제 성공")
    @Test
    void deleteJobPosting_ValidRequest_Success() {
        when(jobPostingRepository.findById(anyLong())).thenReturn(Optional.of(validJobPosting));

        assertDoesNotThrow(() -> jobPostingService.deleteJobPosting(1L));

        verify(jobPostingRepository).deleteById(1L);
    }

    @DisplayName("존재하지 않는 채용공고 삭제 실패")
    @Test
    void deleteJobPosting_InvalidJobPosting_ThrowsException() {
        when(jobPostingRepository.findById(anyLong())).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> jobPostingService.deleteJobPosting(1L));
        assertEquals(ExceptionCode.INVALID_JOBPOSTING_ID.getCode(), exception.getCode());
        assertEquals(ExceptionCode.INVALID_JOBPOSTING_ID.getMessage(), exception.getMessage());
    }

    @DisplayName("모든 채용공고 조회 성공")
    @Test
    void getAllJobPostings_Success() {
        List<JobPosting> jobPostings = Arrays.asList(validJobPosting);
        Page<JobPosting> jobPostingPage = new PageImpl<>(jobPostings);
        when(jobPostingRepository.findJobPostings(any(Pageable.class), anyString())).thenReturn(jobPostingPage);

        Page<JobPostingRes> result = jobPostingService.getAllJobPostings("", Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }


    @DisplayName("채용공고 상세 조회 성공")
    @Test
    void getJobPostingDetail_Success() {
        when(jobPostingRepository.findJobPostingJoinCompany(anyLong())).thenReturn(Optional.of(validJobPosting));

        JobPostingDetailRes result = jobPostingService.getJobPostingDetail(1L);

        assertNotNull(result);
        assertEquals(validJobPosting.getId(), result.getJobPostingId());
        assertEquals(validCompany.getName(), result.getCompanyName());
        assertEquals(validCompany.getCountry(), result.getCountry());
        assertEquals(validCompany.getRegion(), result.getRegion());
        assertEquals(validJobPosting.getPosition(), result.getPosition());
        assertEquals(validJobPosting.getReward(), result.getReward());
        assertEquals(validJobPosting.getTechStack(), result.getTechStack());
        assertEquals(validJobPosting.getDescription(), result.getDescription());

        // jobPostingIdList 검증
        assertNotNull(result.getJobPostingIdList());
        assertTrue(result.getJobPostingIdList().isEmpty()); // 현재 채용공고를 제외한 다른 채용공고가 없으므로 빈 리스트여야 함
    }

    @DisplayName("존재하지 않는 채용공고 상세 조회 실패")
    @Test
    void getJobPostingDetail_InvalidJobPosting_ThrowsException() {
        when(jobPostingRepository.findJobPostingJoinCompany(anyLong())).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> jobPostingService.getJobPostingDetail(1L));
        assertEquals(ExceptionCode.INVALID_JOBPOSTING_ID.getCode(), exception.getCode());
        assertEquals(ExceptionCode.INVALID_JOBPOSTING_ID.getMessage(), exception.getMessage());
    }
}