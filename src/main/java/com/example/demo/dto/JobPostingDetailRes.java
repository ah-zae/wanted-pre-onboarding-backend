package com.example.demo.dto;

import com.example.demo.domain.Company;
import com.example.demo.domain.JobPosting;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class JobPostingDetailRes {
    private Long jobPostingId;
    private String companyName;
    private String country;
    private String region;
    private String position;
    private int reward;
    private String techStack;
    private String description;
    private List<Long> jobPostingIdList;


    public static JobPostingDetailRes fromEntity(JobPosting jobPosting, Company company, List<Long> jobPostingIdList) {
        return JobPostingDetailRes.builder()
                .jobPostingId(jobPosting.getId())
                .companyName(company.getName())
                .country(company.getCountry())
                .region(company.getRegion())
                .position(jobPosting.getPosition())
                .reward(jobPosting.getReward())
                .techStack(jobPosting.getTechStack())
                .description(jobPosting.getDescription())
                .jobPostingIdList(jobPostingIdList)
                .build();
    }




}
