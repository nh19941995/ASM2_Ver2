package com.example.demo.repository;

import com.example.demo.entity.Cv;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CvRepo extends JpaRepository<Cv, Long> {
}
