package com.example.demo.service;

import com.example.demo.entity.ApplyPost;
import com.example.demo.service.crud.*;
import com.example.demo.utility.PaginationRequest;
import org.springframework.data.domain.Page;

public interface ApplyPostService extends
        Count,
        DeleteById<ApplyPost, Long>,
        FindAll<ApplyPost>,
        FindById<ApplyPost, Long>,
        Save<ApplyPost>,
        ApplyNewPost<ApplyPost>
{
    Page<ApplyPost> findAllByCompanyId(Long userId, Long companyId, PaginationRequest paginationRequest);
    Page<ApplyPost> findAllByRecruitmentId(Long recruitmentId, PaginationRequest paginationRequest);
    Page<ApplyPost> findAllAppliedPostsByUser(Long userId, PaginationRequest paginationRequest);
    boolean removeApply(Long userId, Long recruitmentId);
}
