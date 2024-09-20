package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cv")
public class Cv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String fileName;

    // mappedBy = "cv" -> cv là tên thuộc tính trong class User
    @OneToOne(
            mappedBy = "cv",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            },
            fetch = FetchType.LAZY)
    private User user;

    // mappedBy = "cv" -> cv là tên thuộc tính trong class ApplyPost
    @OneToMany(
            mappedBy = "cv",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Set<ApplyPost> applyPosts;

    public Cv(String fileName) {
        this.fileName = fileName;
    }

    public void addUser(User user) {
        this.user = user;
        user.setCv(this);
    }
}
