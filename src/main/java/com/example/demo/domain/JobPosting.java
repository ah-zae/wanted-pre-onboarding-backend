package com.example.demo.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;
    private Integer reward;
    private String description;
    private String techStack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "job")
    private List<Application> applications;


}
