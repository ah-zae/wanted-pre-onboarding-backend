package com.example.demo.dto;

import com.example.demo.domain.Company;
import com.example.demo.domain.JobPosting;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobPostingRes {
    private Long jobPostingId;
    private String companyName;
    private String country;
    private String region;
    private String position;
    private int reward;
    private String techStack;


    public static JobPostingRes fromEntity(JobPosting jobPosting, Company company) {
        return JobPostingRes.builder()
                .jobPostingId(jobPosting.getId())
                .companyName(company.getName())
                .country(company.getCountry())
                .region(company.getRegion())
                .position(jobPosting.getPosition())
                .reward(jobPosting.getReward())
                .techStack(jobPosting.getTechStack())
                .build();
    }

}
