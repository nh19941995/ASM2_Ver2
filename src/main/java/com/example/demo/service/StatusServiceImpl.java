package com.example.demo.service;

import com.example.demo.entity.Status;
import com.example.demo.entity.User;
import com.example.demo.repository.StatusRepo;
import com.example.demo.utility.PaginationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatusServiceImpl implements StatusService {
    private final StatusRepo statusRepo;

    @Autowired
    public StatusServiceImpl(StatusRepo statusRepo) {
        this.statusRepo = statusRepo;
    }

    @Override
    public int count() {
        return (int) statusRepo.count();
    }

    @Override
    public void deleteById(User user, Long id) {
        statusRepo.deleteById(id);
    }

    @Override
    public Page<Status> findAll(PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(
                paginationRequest.getPage(),
                paginationRequest.getSize(),
                Sort.by(paginationRequest.getSortDirection(), paginationRequest.getSortBy())
        );
        return statusRepo.findAll(pageable);
    }

    @Override
    public Iterable<Status> findAll() {
        return statusRepo.findAll();
    }

    @Override
    public Optional<Status> findById(Long id) {
        return statusRepo.findById(id);
    }

    @Override
    public Status save(Status status) {
        return statusRepo.save(status);
    }
}