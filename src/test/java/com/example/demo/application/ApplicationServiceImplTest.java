package com.example.demo.application;

import com.example.demo.domain.Application;
import com.example.demo.domain.JobPosting;
import com.example.demo.domain.User;
import com.example.demo.domain.repository.ApplicationRepository;
import com.example.demo.domain.repository.JobPostingRepository;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.dto.ApplicationReq;
import com.example.demo.global.exception.BadRequestException;
import com.example.demo.global.exception.ExceptionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private JobPostingRepository jobPostingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    private ApplicationReq validRequest;
    private JobPosting validJobPosting;
    private User validUser;

    @BeforeEach
    void setUp() {
        validRequest = new ApplicationReq();
        ReflectionTestUtils.setField(validRequest, "jobPostingId", 1L);
        ReflectionTestUtils.setField(validRequest, "userId", 1L);

        validJobPosting = new JobPosting();
        validUser = new User();
    }

    @DisplayName("유효한 요청으로 지원 성공")
    @Test
    void applyToJob_ValidRequest_Success() {
        // given
        when(jobPostingRepository.findById(anyLong())).thenReturn(Optional.of(validJobPosting));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(validUser));
        when(applicationRepository.existsByUserIdAndJobPostingId(anyLong(), anyLong())).thenReturn(false);

        // when
        assertDoesNotThrow(() -> applicationService.applyToJob(validRequest));

        // then
        verify(applicationRepository).save(any(Application.class));
    }

    @DisplayName("존재하지 않는 채용공고로 지원 실패")
    @Test
    void applyToJob_InvalidJobPosting_ThrowsException() {
        // given
        when(jobPostingRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> applicationService.applyToJob(validRequest));
        assertEquals(ExceptionCode.INVALID_JOBPOSTING_ID.getCode(), exception.getCode());
        assertEquals(ExceptionCode.INVALID_JOBPOSTING_ID.getMessage(), exception.getMessage());
    }

    @DisplayName("존재하지 않는 사용자로 지원 실패")
    @Test
    void applyToJob_InvalidUser_ThrowsException() {
        // given
        when(jobPostingRepository.findById(anyLong())).thenReturn(Optional.of(validJobPosting));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> applicationService.applyToJob(validRequest));
        assertEquals(ExceptionCode.INVALID_USER_ID.getCode(), exception.getCode());
        assertEquals(ExceptionCode.INVALID_USER_ID.getMessage(), exception.getMessage());
    }

    @DisplayName("이미 지원한 채용공고에 재지원 실패")
    @Test
    void applyToJob_AlreadyApplied_ThrowsException() {
        // given
        when(jobPostingRepository.findById(anyLong())).thenReturn(Optional.of(validJobPosting));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(validUser));
        when(applicationRepository.existsByUserIdAndJobPostingId(anyLong(), anyLong())).thenReturn(true);

        // when & then
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> applicationService.applyToJob(validRequest));
        assertEquals(ExceptionCode.ALREADY_APPLY.getCode(), exception.getCode());
        assertEquals(ExceptionCode.ALREADY_APPLY.getMessage(), exception.getMessage());
    }
}