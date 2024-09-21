package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.repository.custom.UserCustomRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long>, UserCustomRepo {
    Optional<User> findByUsername(String username);
    // Tìm tất cả user có statusId < lessThan và cvId > greaterThan
    Page<User> findAllByStatusIdLessThanAndCvIdGreaterThan(Long lessThan, Long greaterThan, Pageable pageable);
}
