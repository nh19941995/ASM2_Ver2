package com.example.demo.repository.custom;

import com.example.demo.entity.ApplyPost;
import com.example.demo.utility.PaginationRequest;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ApplyPostCustomRepo {
    Page<ApplyPost> findAllByComanyId(
            Long companyId, PaginationRequest paginationRequest);
    Page<ApplyPost> findAllByRecruitmentId(
            Long recruitmentId, PaginationRequest paginationRequest);

    Page<ApplyPost> findAllAppliedPostsByUser(
            Long userId, PaginationRequest paginationRequest);

    Optional<ApplyPost> checkExistApplyPost(Long userId, Long recruitmentId);
}
