package com.example.demo.repository.custom;

import com.example.demo.entity.Recruitment;
import com.example.demo.utility.PaginationRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecruitmentCustomRepoImpl implements RecruitmentCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    // Tìm kiếm các bản ghi Recruitment theo companyId và phân trang
    public Page<Recruitment> findAllByCompanyId(PaginationRequest paginationRequest, Long companyId) {
        // Lấy danh sách các bản ghi Recruitment theo companyId
        List<Recruitment> recruitments = fetchRecruitments(paginationRequest, companyId);
        // Đếm số lượng bản ghi Recruitment theo companyId
        long totalRecords = countRecruitments(companyId);
        // Tạo Page từ List
        return createPage(recruitments, paginationRequest, totalRecords);
    }


    // Tìm kiếm các bản ghi Recruitment theo companyId và sắp xếp kết quả
    private List<Recruitment> fetchRecruitments(PaginationRequest paginationRequest, Long companyId) {
        // Tạo câu truy vấn HQL với mệnh đề ORDER BY để sắp xếp kết quả
        String hql = "SELECT r FROM Recruitment r WHERE r.company.id = :companyId ORDER BY r." +
                paginationRequest.getSortBy() + " " + paginationRequest.getSortDirection();

        TypedQuery<Recruitment> query = entityManager.createQuery(hql, Recruitment.class);
        query.setParameter("companyId", companyId);
        // Vị trí bắt đầu lấy bản ghi
        query.setFirstResult(paginationRequest.getPage() * paginationRequest.getSize());
        // Số lượng bản ghi trên mỗi trang
        query.setMaxResults(paginationRequest.getSize());

        return query.getResultList();
    }

    // Đếm số lượng bản ghi Recruitment theo companyId
    private long countRecruitments(Long companyId) {
        String countHql = "SELECT COUNT(r) FROM Recruitment r WHERE r.company.id = :companyId";
        TypedQuery<Long> countQuery = entityManager.createQuery(countHql, Long.class);
        countQuery.setParameter("companyId", companyId);
        return countQuery.getSingleResult();
    }

    // Tạo Page từ List
    private Page<Recruitment> createPage(
            List<Recruitment> recruitments,
            PaginationRequest paginationRequest,
            long totalRecords) {
        Pageable pageable = PageRequest.of(
                paginationRequest.getPage(),
                paginationRequest.getSize(),
                Sort.by(paginationRequest.getSortDirection(), paginationRequest.getSortBy())
        );
        return new PageImpl<>(recruitments, pageable, totalRecords);
    }

    @Override
    public Page<Recruitment> bestPost(PaginationRequest paginationRequest) {
        // Tạo câu truy vấn HQL để tìm kiếm các bản ghi Recruitment có quantity cao nhất
        // và có số người apply nhiều nhất
        String hql = "SELECT r " +
                "FROM Recruitment r " +
                "LEFT JOIN r.applyPosts a " +
                "GROUP BY r " +
                "ORDER BY r.quantity DESC, COUNT(a.id) DESC";

        TypedQuery<Recruitment> query = entityManager.createQuery(hql, Recruitment.class);
        // Vị trí bắt đầu lấy bản ghi
        query.setFirstResult(paginationRequest.getPage() * paginationRequest.getSize());
        // Số lượng bản ghi trên mỗi trang
        query.setMaxResults(paginationRequest.getSize());

        // Lấy danh sách các bản ghi Recruitment
        List<Recruitment> recruitments = query.getResultList();
        // load trước danh sách followers của mỗi bản ghi Recruitment để dùng ở view
        recruitments.forEach(recruitment -> {
            recruitment.getFollowers();
        });
        // Đếm tổng số lượng bản ghi Recruitment
        long totalRecords = countTotal();

        // Tạo Page từ List
        return createPage(recruitments, paginationRequest, totalRecords);
    }

    private Long countTotal() {
        String countJpql = "SELECT COUNT(DISTINCT r) " +
                "FROM Recruitment r ";
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
        return countQuery.getSingleResult();
    }


}
