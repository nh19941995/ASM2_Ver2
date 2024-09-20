package com.example.demo.repository.custom;

import com.example.demo.entity.Company;
import com.example.demo.entity.Recruitment;
import com.example.demo.entity.User;
import com.example.demo.utility.PaginationRequest;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface UserCustomRepo {
    Set<User> findUserApplyPost(Long companyId,int limit);
    Page<Company> findAllFollowedCompanies(Long userId, PaginationRequest paginationRequest);
    Page<Recruitment> findAllFollowedRecruiment (Long userId, PaginationRequest paginationRequest);
    boolean checkExistingPropty(String propertyName, String value);
}
