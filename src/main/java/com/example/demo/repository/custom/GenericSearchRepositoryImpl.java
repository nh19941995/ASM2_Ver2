package com.example.demo.repository.custom;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import jakarta.persistence.EntityManager;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public class GenericSearchRepositoryImpl<T, ID>
        // đây là lớp triển khai mặc định của JpaRepository<T, ID>
        extends SimpleJpaRepository<T, ID>
        implements GenericSearchRepository<T, ID> {

    private final EntityManager entityManager;

    public GenericSearchRepositoryImpl(
            // Đây là một interface trong Spring Data JPA,
            // cung cấp thông tin về entity
            // (như tên bảng, các thuộc tính, và khóa chính).
            JpaEntityInformation<T, ?> entityInformation,
            EntityManager entityManager
    ) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

// code cũ không thể chạy với các trường lồng nhau
//    @Override
//    public List<T> customSearchRepository(
//            String searchTerm,
//            String fieldName,
//            int maxResults
//    ) {
//        // Sử dụng Criteria API để tạo query an toàn hơn
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<T> query = cb.createQuery(getDomainClass());
//        Root<T> root = query.from(getDomainClass());
//
//        // Xây dựng câu lệnh WHERE với điều kiện LIKE và case-insensitive
//        query.select(root)
//                .where(cb.like(cb.lower(root.get(fieldName)), "%" + searchTerm.toLowerCase() + "%"));
//
//        // Thực thi truy vấn, giới hạn số lượng kết quả trả về
//        return entityManager.createQuery(query)
//                .setMaxResults(maxResults)  // Giới hạn số kết quả trả về
//                .getResultList();
//    }


    @Override
    public List<T> customSearchRepository(String searchTerm, String fieldName, int maxResults) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(getDomainClass());
        Root<T> root = query.from(getDomainClass());

        // Tách chuỗi fieldName thành mảng nestedFields
        // chia chuỗi "company.nameCompany" thành mảng ["company", "nameCompany"]
        String[] nestedFields = fieldName.split("\\.");
        // Duyệt qua các trường lồng nhau:
        // root.get("company").get("nameCompany")
        Path<String> path = root.get(nestedFields[0]);
        for (int i = 1; i < nestedFields.length; i++) {
            path = path.get(nestedFields[i]);
        }
        // Xây dựng câu lệnh
        query.select(root)
            .where(
                // phương thức để kết hợp nhiều điều kiện AND
                cb.and(
                    // điều kiện LIKE
                    cb.like(
                        // chuyển đổi chuỗi thành chữ thường
                        cb.lower(path.as(String.class)),
                        // thêm ký tự % vào trước và sau chuỗi tìm kiếm
                        "%" + searchTerm.toLowerCase() + "%"
                    ),
                    // điều kiện statusId < 3
                    cb.lessThan(root.get("statusId"), 3)
                )
            );
        return entityManager.createQuery(query)
                .setMaxResults(maxResults)
                .getResultList();
    }

}



