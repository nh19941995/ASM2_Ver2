package com.example.demo.repository.custom;

import com.example.demo.entity.Recruitment;
import com.example.demo.utility.PaginationRequest;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface RecruitmentCustomRepo {
    Page<Recruitment> findAllByCompanyId(PaginationRequest paginationRequest, Long companyId);
    Page<Recruitment> bestPost(PaginationRequest paginationRequest);
}
