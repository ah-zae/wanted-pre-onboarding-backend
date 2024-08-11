package com.example.demo.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;
    private String region;

    @OneToMany(mappedBy = "company")
    private List<JobPosting> jobPostings;
}
