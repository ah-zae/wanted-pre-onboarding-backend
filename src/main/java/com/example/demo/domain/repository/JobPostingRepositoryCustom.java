package com.example.demo.domain.repository;

import com.example.demo.domain.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobPostingRepositoryCustom {

    Page<JobPosting> findJobPostings(Pageable pageable,String search);
}
