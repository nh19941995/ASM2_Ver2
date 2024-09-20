package com.example.demo.service;

import com.example.demo.entity.Type;
import com.example.demo.entity.User;
import com.example.demo.repository.TypeRepo;
import com.example.demo.utility.PaginationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TypeServiceImpl implements TypeService {
    private final TypeRepo typeRepo;

    @Autowired
    public TypeServiceImpl(TypeRepo typeRepo) {
        this.typeRepo = typeRepo;
    }

    @Override
    public void deleteById(User user, Long id) {
        typeRepo.deleteById(id);
    }

    @Override
    public Page<Type> findAll(PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(
                paginationRequest.getPage(),
                paginationRequest.getSize(),
                Sort.by(paginationRequest.getSortDirection(), paginationRequest.getSortBy())
        );
        return typeRepo.findAll(pageable);
    }

    @Override
    public Iterable<Type> findAll() {
        return typeRepo.findAll();
    }

    @Override
    public Optional<Type> findById(Long id) {
        return typeRepo.findById(id);
    }

    @Override
    public Type save(Type type) {
        return typeRepo.save(type);
    }

    @Override
    public int count() {
        return (int) typeRepo.count();
    }
}