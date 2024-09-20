package com.example.demo.repository.custom;

import com.example.demo.entity.ApplyPost;
import com.example.demo.utility.PaginationRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ApplyPostCustomRepoImpl implements ApplyPostCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ApplyPost> findAllByComanyId(Long companyId, PaginationRequest paginationRequest) {
        return findAllByCondition("a.recruitment.company.id", companyId, paginationRequest);
    }

    @Override
    public Page<ApplyPost> findAllByRecruitmentId(Long recruitmentId, PaginationRequest paginationRequest) {
        return findAllByCondition("a.recruitment.id", recruitmentId, paginationRequest);
    }

    @Override
    public Page<ApplyPost> findAllAppliedPostsByUser(Long userId, PaginationRequest paginationRequest) {
        return findAllByCondition("a.cv.user.id", userId, paginationRequest);
    }

    private Page<ApplyPost> findAllByCondition(String condition, Long value, PaginationRequest paginationRequest) {
        List<ApplyPost> applyPosts = fetchApplyPosts(condition, value, paginationRequest);
        long totalRecords = countApplyPosts(condition, value);
        return createPage(applyPosts, paginationRequest, totalRecords);
    }

    // kiểm tra xem bài viết đã ứng tuyển đã tồn tại hay chưa
    @Override
    public Optional<ApplyPost> checkExistApplyPost(Long userId, Long recruitmentId) {
        String hql = "SELECT a FROM ApplyPost a WHERE a.cv.user.id = :userId AND a.recruitment.id = :recruitmentId";
        TypedQuery<ApplyPost> query = entityManager.createQuery(hql, ApplyPost.class);
        query.setParameter("userId", userId);
        query.setParameter("recruitmentId", recruitmentId);
        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // lây danh sách các bài viết đã ứng tuyển theo đều kiện (condition) và giá trị (value) cụ thể
    private List<ApplyPost> fetchApplyPosts(String condition, Long value, PaginationRequest paginationRequest) {
        String hql = "SELECT a FROM ApplyPost a WHERE " + condition + " = :value " +
                "ORDER BY a." + paginationRequest.getSortBy() + " " + paginationRequest.getSortDirection();

        TypedQuery<ApplyPost> query = entityManager.createQuery(hql, ApplyPost.class);
        query.setParameter("value", value);
        query.setFirstResult(paginationRequest.getPage() * paginationRequest.getSize());
        query.setMaxResults(paginationRequest.getSize());
        return query.getResultList();
    }

    // đếm số lượng bài viết đã ứng tuyển theo thuộc tính (condition) và giá trị (value) cụ thể
    private long countApplyPosts(String condition, Long value) {
        String countHql = "SELECT COUNT(a) FROM ApplyPost a WHERE " + condition + " = :value";
        TypedQuery<Long> countQuery = entityManager.createQuery(countHql, Long.class);
        countQuery.setParameter("value", value);
        return countQuery.getSingleResult();
    }

    // tạo trang mới
    private Page<ApplyPost> createPage(List<ApplyPost> resultList, PaginationRequest paginationRequest, long totalRecords) {
        Pageable pageable = PageRequest.of(
                paginationRequest.getPage(),
                paginationRequest.getSize(),
                Sort.by(paginationRequest.getSortDirection(), paginationRequest.getSortBy())
        );
        return new PageImpl<>(resultList, pageable, totalRecords);
    }
}