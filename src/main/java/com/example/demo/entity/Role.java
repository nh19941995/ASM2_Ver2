package com.example.demo.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "is required")
    private String roleName;

    @NotBlank(message = "is required")
    private String classCss;

    // mappedBy = "role" -> role là tên thuộc tính trong class User
    @OneToMany(
            mappedBy = "role",
            cascade = {
                CascadeType.PERSIST,
                CascadeType.MERGE,
                CascadeType.REFRESH,
                CascadeType.DETACH
            },
            fetch = FetchType.LAZY
    )
    private List<User> users;

    public Role(String roleName, String classCss) {
        this.roleName = roleName;
        this.classCss = classCss;
    }
}


