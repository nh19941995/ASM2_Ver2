package com.example.demo.repository;

import com.example.demo.entity.ApplyPost;
import com.example.demo.repository.custom.ApplyPostCustomRepo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyPostRepo extends
        JpaRepository<ApplyPost, Long>, ApplyPostCustomRepo {
}
