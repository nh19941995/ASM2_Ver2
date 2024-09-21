package com.example.demo.service;

import com.example.demo.entity.Recruitment;
import com.example.demo.entity.User;
import com.example.demo.utility.PaginationRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.example.demo.service.crud.*;

@Service
public interface RecruitmentService extends
        Count,
        DeleteById<User, Long>,
        FindAll<Recruitment>,
        FindById<Recruitment, Long>,
        Save<Recruitment>,
        CustomSearchRepository<Recruitment>
{
    public Page<Recruitment> findAllByCompanyId(PaginationRequest paginationRequest, Long companyId);
    public Page<Recruitment> bestPost(PaginationRequest paginationRequest);
    public Page<Recruitment> findAll(PaginationRequest paginationRequest);

}
