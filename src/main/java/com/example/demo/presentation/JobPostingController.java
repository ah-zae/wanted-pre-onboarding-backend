package com.example.demo.presentation;

import com.example.demo.application.JobPostingService;
import com.example.demo.dto.JobPostingDetailRes;
import com.example.demo.dto.JobPostingReq;
import com.example.demo.dto.JobPostingRes;
import com.example.demo.dto.JobPostingUpdateReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobPostings")
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @PostMapping
    public ResponseEntity<Long> createJobPosting(@RequestBody @Valid JobPostingReq jobPostingReq) {
        return ResponseEntity.ok(jobPostingService.createJobPosting(jobPostingReq));
    }

    @PutMapping("/{jobPostingId}")
    public ResponseEntity<String> updateJobPosting(@PathVariable Long jobPostingId, @RequestBody JobPostingUpdateReq jobPostingUpdateReq) {
        jobPostingService.updateJobPosting(jobPostingId, jobPostingUpdateReq);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/{jobPostingId}")
    public ResponseEntity<String> deleteJobPosting(@PathVariable Long jobPostingId) {
        jobPostingService.deleteJobPosting(jobPostingId);
        return ResponseEntity.ok("success");
    }

    @GetMapping
    public ResponseEntity<Page<JobPostingRes>> getAllJobPostings(@RequestParam(required = false) String search,Pageable pageable) {
        return ResponseEntity.ok(jobPostingService.getAllJobPostings(search,pageable));
    }

    @GetMapping("/{jobPostingId}")
    public JobPostingDetailRes getJobDetail(@PathVariable Long jobPostingId) {
        return jobPostingService.getJobPostingDetail(jobPostingId);
    }
}
