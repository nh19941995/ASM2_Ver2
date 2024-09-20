package com.example.demo.entity;

import com.example.demo.controller.dto.UserUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "is required")
    private String fullName;

    @Column(unique = true)
    @NotBlank(message = "is required")
    private String username;

    private String token;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*\\d).{8,}$",
            message = "Password must be at least 8 characters long, " +
                    "contain at least one uppercase letter, " +
                    "one lowercase letter, one number, and one special character."
    )
    private String password;

    @Column(unique = true)
    @NotBlank(message = "is required")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "is required")
    private String address;

    private String image;

    @Column(unique = true)
    @NotBlank(message = "is required")
    @Pattern(regexp = "^\\d{10}$", message = "phone must be 10 digits")
    private String phone;

    @NotBlank(message = "is required")
    private String description;

    @ManyToOne(
            cascade = {
                CascadeType.DETACH,
                CascadeType.MERGE,
                CascadeType.PERSIST,
                CascadeType.REFRESH
            }
    )
    // tên cột chứa khóa phụ trong bảng user là status_id
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne(
            fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH
    })
    // tên cột chứa khóa phụ trong bảng user là role_id
    // cột phụ role_id sẽ dc thêm vào bảng user
    @JoinColumn(
            name = "role_id"
    )
    private Role role;

    @OneToOne
    // tên cột chứa khóa phụ trong bảng user là cv_id
    // cột phụ cv_id sẽ dc thêm vào bảng user
    @JoinColumn(name = "cv_id")
    private Cv cv;

    // tên cột chứa khóa phụ trong bảng user là company_id
    // cột phụ company_id sẽ dc thêm vào bảng user
    @OneToOne(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            },
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "company_id")
    private Company company;


    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            }
    )
    @JoinTable(
            // tên bảng trung gian để lưu thông tin người dùng theo dõi công ty
            name = "company_followers",
            // tên cột chứa khóa chính trong bảng trung gian
            joinColumns = @JoinColumn(name = "user_id"),
            // tên cột chứa khóa phụ trong bảng trung gian
            inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    private Set<Company> followedCompanies = new HashSet<>();

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            }
    )
    @JoinTable(
            // tên bảng trung gian để lưu thông tin người dùng theo dõi bài đăng
            name = "recruiment_followers",
            // tên cột chứa khóa chính trong bảng trung gian
            joinColumns = @JoinColumn(name = "user_id"),
            // tên cột chứa khóa phụ trong bảng trung gian
            inverseJoinColumns = @JoinColumn(name = "recruiment_id")
    )
    private Set<Recruitment> recruitments = new HashSet<>();


    public User(UserUpdate userUpdate) {
        this.id = userUpdate.getId();
        this.fullName = userUpdate.getFullName();
        this.email = userUpdate.getEmail();
        this.address = userUpdate.getAddress();
        this.image = userUpdate.getImage();
        this.phone = userUpdate.getPhone();
        this.description = userUpdate.getDescription();
        this.username = userUpdate.getUsername();
    }

    public void removeCv() {
        if (cv != null) {
            cv.setUser(null);
            cv = null;
        }
    }

    public void removeFollowCompany(Company company) {
        if (company != null) {
            // xóa phía user
            followedCompanies.remove(company);
            // xóa phía company
            company.getFollowers().remove(this);
        }
    }

    public void followNewCompany(Company company) {
        if (company != null) {
            // thêm phía user
            followedCompanies.add(company);
            // thêm phía company
            company.getFollowers().add(this);
        }
    }

    public void removeFollowRecruitment(Recruitment recruitment) {
        if (recruitment != null) {
            // xóa phía user
            recruitments.remove(recruitment);
            // xóa phía recruitment
            recruitment.getFollowers().remove(this);
        }
    }

    public void followNewRecruitment(Recruitment recruitment) {
        if (recruitment != null) {
            // thêm phía user
            recruitments.add(recruitment);
            // thêm phía recruitment
            recruitment.getFollowers().add(this);
        }
    }
}
