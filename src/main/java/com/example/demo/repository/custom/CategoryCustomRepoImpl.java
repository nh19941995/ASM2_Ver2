package com.example.demo.repository.custom;

import com.example.demo.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CategoryCustomRepoImpl implements CategoryCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Category> findTopList(int days) {
        // Tính toán ngày bắt đầu (days ngày trước)
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);

        // Viết HQL/JPQL truy vấn
        String hql = "select r.category " +
                "from Recruitment r " +
                "where r.createdAt >= :startDate " +
                "group by r.category.id " +
                "order by sum(r.quantity) desc";

        // Tạo truy vấn với kiểu trả về cụ thể
        TypedQuery<Category> query = entityManager.createQuery(hql, Category.class);
        query.setParameter("startDate", startDate);
        query.setMaxResults(4);

        // Trả về kết quả với kiểu cụ thể
        return query.getResultList();
    }


    @Override
    public List<Category> findTopList() {
        // lấy ra danh sách các category có số lượng tuyển dụng nhiều nhất
        // trong days ngày gần nhất
        String hql = "select c " +
                "from Category c " +
                "order by c.numberChoose desc";

        // Tạo truy vấn với kiểu trả về cụ thể
        TypedQuery<Category> query = entityManager.createQuery(hql, Category.class);
        query.setMaxResults(4);

        // Trả về kết quả với kiểu cụ thể
        return query.getResultList();
    }


}
