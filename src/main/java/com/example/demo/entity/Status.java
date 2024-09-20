package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "status")
public class Status {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank(message = "is required")
        private String statusName;

        @NotBlank(message = "is required")
        private String description;

        @NotBlank(message = "is required")
        private String classCss;

        // status là tên biến trong class User
        @OneToMany(mappedBy = "status")
        private List<User> users;

        // status là tên biến trong class ApplyPost
        @OneToMany(mappedBy = "status")
        private List<ApplyPost> applyPosts;

        // status là tên biến trong class Company
        @OneToMany(mappedBy = "status")
        private List<Company> companies;

        // status là tên biến trong class Recruitment
        @OneToMany(mappedBy = "status")
        private List<Recruitment> recruitments;

        public Status(String statusName, String description, String classCss) {
                this.statusName = statusName;
                this.description = description;
                this.classCss = classCss;
        }

        public void addUser(User user) {
                if (users == null) {
                        users = new ArrayList<>();
                }
                users.add(user);
                user.setStatus(this);
        }
}
