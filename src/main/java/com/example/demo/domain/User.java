package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Application> applications;

    // 연관관계 편의 메소드
    public void addApplication(Application application) {
        applications.add(application);
        application.setUser(this);
    }

    public void removeApplication(Application application) {
        applications.remove(application);
        application.setUser(null);
    }
}
