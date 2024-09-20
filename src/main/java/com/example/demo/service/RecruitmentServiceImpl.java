package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RecruitmentRepo;
import com.example.demo.repository.StatusRepo;
import com.example.demo.utility.PaginationRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

@Service
public class RecruitmentServiceImpl implements RecruitmentService {

    private final RecruitmentRepo recruitmentRepo;
    private final StatusRepo statusRepo;
    private final CompanyService companyService;
    private final TypeService typeService;
    private final CategoryService categoryService;

    @Autowired
    public RecruitmentServiceImpl(RecruitmentRepo recruitmentRepo, StatusRepo statusRepo, CompanyService companyService, TypeService typeService, CategoryService categoryService) {
        this.recruitmentRepo = recruitmentRepo;
        this.statusRepo = statusRepo;
        this.companyService = companyService;
        this.typeService = typeService;
        this.categoryService = categoryService;
    }


    @Override
    public Iterable<Recruitment> findAll() {
        return recruitmentRepo.findAll();
    }

    @Transactional
    @Override
    public Recruitment save(Recruitment recruitment) {
        // thêm thời gian tạo mới
        recruitment.setCreatedAt(LocalDateTime.now());
        // thêm status cho recruitment
        Status status = statusRepo.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Status not found"));
        recruitment.setStatus(status);
        // thêm type cho recruitment
        Type type = typeService.findById(recruitment.getType().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Type not found"));
        recruitment.setType(type);
        // thêm category cho recruitment
        Category category = categoryService.findById(recruitment.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        recruitment.setCategory(category);

        // thêm company cho recruitment
        Company company = companyService.findById(recruitment.getCompany().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        recruitment.setCompany(company);
        return recruitmentRepo.save(recruitment);
    }

    @Override
    public Optional<Recruitment> findById(Long id) {
        return recruitmentRepo.findById(id);
    }

    @Override
    public Page<Recruitment> findAll(PaginationRequest paginationRequest) {
        Page<Recruitment> recruitmentPage = recruitmentRepo.findAll(paginationRequest.toPageable());

        if (paginationRequest.getPage() > recruitmentPage.getTotalPages()) {
            throw new ResourceNotFoundException("The requested page does not exist");
        }
        return recruitmentPage;
    }

    @Override
    public int count() {
        return (int) recruitmentRepo.count();
    }

    @Transactional
    @Override
    public void deleteById(User user, Long id) {
        // check quyền xóa
        Recruitment recruitment = recruitmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recruitment not found"));

        if (recruitment.getCompany() == null
                || recruitment.getCompany().getUser() == null
                || !recruitment.getCompany().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You don't have permission to delete this recruitment");
        }

        recruitmentRepo.deleteById(id);
    }

    @Override
    public Page<Recruitment> findAllByCompanyId(PaginationRequest paginationRequest, Long companyId) {

        Page<Recruitment> recruitmentPage = recruitmentRepo.findAllByCompanyId(paginationRequest, companyId);

        if (paginationRequest.getPage() > recruitmentPage.getTotalPages()) {
            throw new ResourceNotFoundException("The requested page does not exist");
        }
        return recruitmentPage;
    }

    @Override
    public Page<Recruitment> bestPost(PaginationRequest paginationRequest) {
        return recruitmentRepo.bestPost(paginationRequest);
    }


    @Override
    public Page<Recruitment> searchByField(
            String searchValue,
            String propertyName,
            PaginationRequest paginationRequest
    ) {
        List<Recruitment> results = recruitmentRepo.
                customSearchRepository(searchValue, propertyName, 1000);
        Pageable pageable = paginationRequest.toPageable();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), results.size());
        return new PageImpl<>(results.subList(start, end), pageable, results.size());
    }


}