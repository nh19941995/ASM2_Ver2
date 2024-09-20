package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Description is required")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    private String logo;

    @NotBlank(message = "Company name is required")
    private String nameCompany;

    @NotBlank(message = "is required")
    @Pattern(regexp = "^\\d{10}$", message = "phone must be 10 digits")
    private String phoneNumber;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            }
    )
    // name = "status_id" -> status_id là tên cột trong bảng company
    // status_id là khóa phụ tham chiếu đến khóa chính id trong bảng status
    @JoinColumn(name = "status_id")
    private Status status;

    //     mappedBy = "company" -> company là tên thuộc tính trong class User
    @OneToOne(
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            },
            mappedBy = "company"
    )
    private User user;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            },
            // tên biến companies trong entity User
            mappedBy = "followedCompanies"
    )
    // tên biến authors trong entity Book
    private Set<User> followers = new HashSet<>();

    @OneToMany(
            mappedBy = "company",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            },
            fetch = FetchType.LAZY)
    private Set<Recruitment> recruitments = new HashSet<>();

    public void addUser(User user) {
        this.user = user;
        user.setCompany(this);
    }

    public void removeFollowers(User user) {
        if (user != null) {
            this.followers.remove(user);
            user.getFollowedCompanies().remove(this);
        }
    }

    public void addFollowers(User user) {
        if (user != null) {
            this.followers.add(user);
            user.getFollowedCompanies().add(this);
        }
    }
}