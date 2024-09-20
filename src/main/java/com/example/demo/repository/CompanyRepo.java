package com.example.demo.repository;

import com.example.demo.entity.Company;
import com.example.demo.repository.custom.CompanyCustomRepo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepo extends JpaRepository<Company, Long>, CompanyCustomRepo {
}
