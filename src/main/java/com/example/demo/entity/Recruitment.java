package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recruitment")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Recruitment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Address is required")
    private String address;

    private LocalDateTime createdAt;

    @NotBlank(message = "Description is required")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Experience is required")
    private String experience;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String recruitmentRank;

    @NotBlank(message = "Salary is required")
    private String salary;

    @ManyToOne(
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH
            }
    )
    // tên cột chứa khóa phụ trong bảng recruitment là status_id
    @JoinColumn(name = "status_id")
    private Status status;

    @NotBlank(message = "Title is required")
    private String title;

    private Integer view;

    @NotNull(message = "Deadline is required")
    private LocalDate deadline;

    // type_id là tên cột trong bảng recruitment (khóa phụ)
    // bảng recruitment sẽ them cột type_id
    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "type_id")
    private Type type;

    // company_id là tên cột trong bảng recruitment (khóa phụ)
    // bảng recruitment sẽ them cột company_id
    @ManyToOne(
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;

    // category_id là tên cột trong bảng recruitment (khóa phụ)
    // bảng recruitment sẽ them cột category_id
    @ManyToOne(
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH
            },
            fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // apply_post_id là tên cột trong bảng recruitment (khóa phụ)
    // bảng recruitment sẽ them cột apply_post_id
    @OneToMany(
            mappedBy = "recruitment",
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE,
                CascadeType.REFRESH,
                CascadeType.DETACH
            },
            fetch = FetchType.LAZY
    )
    private Set<ApplyPost> applyPosts = new HashSet<>();

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            },
            // tên biến recruitments trong entity User
            mappedBy = "recruitments"
    )
    // tên biến authors trong entity Book
    private Set<User> followers = new HashSet<>();

    public void addFollower(User user) {
        if (user != null) {
            followers.add(user);
            user.getRecruitments().add(this);
        }
    }

    public void removeFollower(User user) {
        if (user != null) {
            followers.remove(user);
            user.getRecruitments().remove(this);
        }
    }
}
