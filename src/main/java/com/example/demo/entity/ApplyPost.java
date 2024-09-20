package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "apply_post")
public class ApplyPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate createdDate;

    @NotBlank(message = "is required")
    private String text;


    // status_id là tên cột trong bảng apply_post (khóa phụ)
    @ManyToOne(
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH
            },
            fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status status;

    // recruitment_id là tên cột trong bảng apply_post (khóa phụ)
    // bảng apply_post se them cot recruitment_id
    @ManyToOne(
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH
            },
            fetch = FetchType.EAGER)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @ManyToOne(
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH
            },
            fetch = FetchType.EAGER)
    @JoinColumn(name = "cv_id")
    private Cv cv;


}
