package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepo;
import com.example.demo.utility.PaginationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    @Autowired
    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }


    @Override
    public int count() {
       return (int)categoryRepo.count();
    }

    @Override
    public void deleteById(Category category, Long id) {
        categoryRepo.deleteById(id);
    }

    @Override
    public Page<Category> findAll(PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(
                paginationRequest.getPage(),
                paginationRequest.getSize(),
                Sort.by(
                        paginationRequest.getSortDirection(),
                        paginationRequest.getSortBy())
                );
        return categoryRepo.findAll(pageable);
    }

    @Override
    public Iterable<Category> findAll() {
        return categoryRepo.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepo.findById(id);
    }

    @Override
    public Category save(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    public List<Category> findTopList(int days) {
        return categoryRepo.findTopList(days);
    }

    @Override
    public List<Category> findTopList() {
        return categoryRepo.findTopList();
    }
}
