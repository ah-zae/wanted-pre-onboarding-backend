package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "jobPosting")
@NoArgsConstructor
@Getter
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String position;

    @Setter
    private Integer reward;

    @Setter
    private String description;

    @Setter
    private String techStack;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "jobPosting",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Application> applications;


    @Builder
    public JobPosting(String position, Integer reward, String description, String techStack, Company company) {
        this.position = position;
        this.reward = reward;
        this.description = description;
        this.techStack = techStack;
        this.company = company;
    }

    // 연관관계 편의 메소드
    public void addApplication(Application application) {
        applications.add(application);
        application.setJobPosting(this);
    }

    public void removeApplication(Application application) {
        applications.remove(application);
        application.setJobPosting(null);
    }


}
