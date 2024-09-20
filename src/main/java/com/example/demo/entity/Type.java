package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "type")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "is required")
    private String name;

    @NotBlank(message = "is required")
    private String description;

    // mappedBy trỏ tới tên biến type ở trong class Recruitment
    @OneToMany(
            mappedBy = "type",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            },
            fetch = FetchType.LAZY)
    private List<Recruitment> recruitment;

    public Type(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
