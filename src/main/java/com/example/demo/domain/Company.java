package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "company")
@NoArgsConstructor
@Getter
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;
    private String region;

    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<JobPosting> jobPostings;

    // 연관관계 편의 메소드
    public void addJobPosting(JobPosting jobPosting) {
        jobPostings.add(jobPosting);
        jobPosting.setCompany(this);
    }

    public void removeJobPosting(JobPosting jobPosting) {
        jobPostings.remove(jobPosting);
        jobPosting.setCompany(null);
    }
}
