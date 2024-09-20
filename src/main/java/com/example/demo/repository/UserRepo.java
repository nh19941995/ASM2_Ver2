package com.example.demo.repository;

import com.example.demo.entity.Company;
import com.example.demo.entity.User;
import com.example.demo.repository.custom.UserCustomRepo;
import com.example.demo.utility.PaginationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface UserRepo extends JpaRepository<User, Long>, UserCustomRepo {
    Optional<User> findByUsername(String username);
}
