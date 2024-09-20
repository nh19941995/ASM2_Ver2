package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "save_job")
public class SaveJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // recruitment_id là tên cột trong bảng save_job (khóa phụ)
    @OneToOne
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    // user_id là tên cột trong bảng save_job (khóa phụ)
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
