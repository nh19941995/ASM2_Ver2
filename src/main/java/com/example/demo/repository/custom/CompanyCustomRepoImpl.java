package com.example.demo.repository.custom;

import com.example.demo.entity.Company;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class CompanyCustomRepoImpl implements CompanyCustomRepo {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CompanyCustomRepoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<Company> findTop(Pageable pageable) {
        // tạo câu lệnh truy vấn tìm ra công ty tuyển nhiều nhất
        String jpql = "SELECT c " +
                "FROM " +
                    "Company c " +
                // lấy dữ liệu khớp ở cả 2 bảng
                "JOIN " +
                    "c.recruitments r " +
                "WHERE " +
                    "r.createdAt > :oneYearAgo " +
                "GROUP BY " +
                    "c.id " +
                "ORDER BY " +
                    "SUM(r.quantity) DESC";


        String jpql2 = "SELECT c " +
                "FROM " +
                    "Recruitment r " +
                "LEFT JOIN " +
                    "Company c ON r.company.id = c.id " +
                "WHERE " +
                    "r.createdAt > :oneYearAgo " +
                "GROUP BY " +
                    "c.id " +
                "ORDER BY " +
                    "SUM(r.quantity) DESC";

        // tạo query và thêm tham số truy vấn
        TypedQuery<Company> query = entityManager.createQuery(jpql, Company.class);
        TypedQuery<Company> query2 = entityManager.createQuery(jpql2, Company.class);
        // thêm tham số truy vấn oneYearAgo là ngày hiện tại trừ đi 1 năm
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        query.setParameter("oneYearAgo", oneYearAgo);

        // Lấy tổng số bản ghi
        Long totalRows = countTotalRecruitingCompanies(oneYearAgo);

        // set vị trí bắt đầu và số lượng bản ghi trên mỗi trang
        query.setFirstResult((int) pageable.getOffset());
        // set số lượng bản ghi trên mỗi trang
        query.setMaxResults(pageable.getPageSize());

        // Tạo Page bằng phương thức riêng
        return createPage(query.getResultList(), pageable, totalRows);
    }

    private Long countTotalRecruitingCompanies(LocalDate oneYearAgo) {
        String countJpql = "SELECT COUNT(DISTINCT c) " +
                "FROM Company c " +
                "JOIN c.recruitments r " +
                "WHERE r.createdAt > :oneYearAgo";

        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
        countQuery.setParameter("oneYearAgo", oneYearAgo);

        return countQuery.getSingleResult();
    }

    private Page<Company> createPage(List<Company> content, Pageable pageable, Long total) {
        return new PageImpl<>(content, pageable, total);
    }
}

// câu lệnh này sẽ nhanh hơn vì nó bắt đầu từ bảng company(bảng ít dữ liệu hơn)
// và nó ko lấy ra các recruitments có quantity = 0
// và nó chỉ lấy ra dữ liệu khớp ở cả 2 bảng

//String jpql = "SELECT c " +
//        "FROM " +
//        "Company c " +
//        // lấy dữ liệu khớp ở cả 2 bảng
//        "JOIN " +
//        "c.recruitments r " +
//        "WHERE " +
//        "r.createdAt > :oneYearAgo " +
//        "GROUP BY " +
//        "c.id " +
//        "ORDER BY " +
//        "SUM(r.quantity) DESC";


// câu lệnh này sẽ chậm hơn vì nó bắt đầu từ bảng recruitment(bảng lớn hơn)
// và nó lấy ra tất cả các recruitments kể cả có quantity = 0
// trường hợp recruitment không có company thì nó vẫn giữ lại nên sẽ thừa dữ liệu

//String jpql2 = "SELECT c " +
//        "FROM " +
//        "Recruitment r " +
//        "LEFT JOIN " +
//        "Company c ON r.company.id = c.id " +
//        "WHERE " +
//        "r.createdAt > :oneYearAgo " +
//        "GROUP BY " +
//        "c.id " +
//        "ORDER BY " +
//        "SUM(r.quantity) DESC";