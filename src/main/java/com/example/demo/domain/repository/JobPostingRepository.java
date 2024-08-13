package com.example.demo.domain.repository;

import com.example.demo.domain.Company;
import com.example.demo.domain.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface JobPostingRepository extends JpaRepository<JobPosting, Long>,JobPostingRepositoryCustom {
    @Query("""
            select j from JobPosting 
            j join fetch j.company
            where j.id = :jobPostingId
            """)
    Optional<JobPosting> findJobPostingJoinCompany(Long jobPostingId);
}
