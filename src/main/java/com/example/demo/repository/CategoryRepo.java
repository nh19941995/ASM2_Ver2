package com.example.demo.repository;

import com.example.demo.entity.Category;

import com.example.demo.repository.custom.CategoryCustomRepo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long>, CategoryCustomRepo {
}
