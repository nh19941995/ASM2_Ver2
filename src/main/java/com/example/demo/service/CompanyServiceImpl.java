package com.example.demo.service;

import com.example.demo.entity.Company;
import com.example.demo.entity.Status;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CompanyRepo;
import com.example.demo.repository.StatusRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.utility.PaginationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepo companyRepo;
    private final StatusRepo statusRepo;
    private final UserRepo userRepo;

    @Autowired
    public CompanyServiceImpl(CompanyRepo companyRepo, StatusRepo statusRepo, UserRepo userRepo) {
        this.companyRepo = companyRepo;
        this.statusRepo = statusRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Iterable<Company> findAll() {
        return companyRepo.findAll();
    }

    @Override
    public Company save(Company company) {
        Status status = statusRepo.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Status not found"));

        User user = userRepo.findById(company.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        company.setStatus(status);
        company.addUser(user);

        return companyRepo.save(company);
    }

    @Override
    public Optional<Company> findById(Long id) {
        return companyRepo.findById(id);
    }

    @Override
    public Page<Company> findAll(PaginationRequest paginationRequest) {
        Page<Company> companyPage = companyRepo.findAll(paginationRequest.toPageable());

        if (paginationRequest.getPage() >= companyPage.getTotalPages()) {
            throw new ResourceNotFoundException("The requested page does not exist");
        }
        return companyPage;
    }

    @Override
    public int count() {
        return (int) companyRepo.count();
    }

    @Override
    public void deleteById(Company company, Long id) {
        companyRepo.deleteById(id);
    }

    @Override
    public Page<Company> findTop(PaginationRequest paginationRequest) {
        return companyRepo.findTop(paginationRequest.toPageable());
    }
}
