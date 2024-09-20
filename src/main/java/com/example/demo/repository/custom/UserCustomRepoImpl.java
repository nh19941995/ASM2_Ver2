package com.example.demo.repository.custom;

import com.example.demo.entity.Company;
import com.example.demo.entity.Recruitment;
import com.example.demo.entity.User;
import com.example.demo.utility.PaginationRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserCustomRepoImpl implements UserCustomRepo {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Set<User> findUserApplyPost(Long companyId, int limit) {
        String hql = "SELECT DISTINCT u FROM Company c " +
                "LEFT JOIN c.user u " +
                "LEFT JOIN c.recruitments r " +
                "LEFT JOIN r.applyPosts a " +
                "WHERE c.id = :companyId";

        TypedQuery<User> query = entityManager.createQuery(hql, User.class);
        query.setParameter("companyId", companyId);
        query.setMaxResults(limit);

        return new HashSet<>(query.getResultList());
    }

    @Override
    public Page<Company> findAllFollowedCompanies(Long userId, PaginationRequest paginationRequest) {
        List<Company> companies = fetchFollowedEntities(userId, paginationRequest, Company.class, "c.followers");
        long totalRecords = countFollowedEntities(userId, Company.class, "c.followers");
        return createPage(companies, paginationRequest, totalRecords);
    }

    @Override
    public Page<Recruitment> findAllFollowedRecruiment(Long userId, PaginationRequest paginationRequest) {
        List<Recruitment> recruitments = fetchFollowedEntities(userId, paginationRequest, Recruitment.class, "r.followers");
        long totalRecords = countFollowedEntities(userId, Recruitment.class, "r.followers");
        return createPage(recruitments, paginationRequest, totalRecords);
    }

    @Override
    public boolean checkExistingPropty(String propertyName, String value) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<User> user = query.from(User.class);

        // Tạo điều kiện tìm kiếm thuộc tính dựa trên tên thuộc tính và giá trị
        Predicate condition = cb.equal(user.get(propertyName), value);

        // Đếm số lượng bản ghi tìm thấy
        query.select(cb.count(user)).where(condition);

        Long count = entityManager.createQuery(query).getSingleResult();

        // Trả về true nếu tìm thấy ít nhất 1 bản ghi
        return count > 0;
    }

    private <T> List<T> fetchFollowedEntities(Long userId, PaginationRequest paginationRequest, Class<T> entityClass, String joinPath) {
        String entityAlias = entityClass.getSimpleName().substring(0, 1).toLowerCase();
        String hql = String.format("SELECT %s FROM %s %s JOIN %s u WHERE u.id = :userId", entityAlias, entityClass.getSimpleName(), entityAlias, joinPath);

        TypedQuery<T> query = entityManager.createQuery(hql, entityClass);
        query.setParameter("userId", userId);
        query.setFirstResult(paginationRequest.getPage() * paginationRequest.getSize());
        query.setMaxResults(paginationRequest.getSize());

        return query.getResultList();
    }

    // hàm đếm số lượng các entity mà user đã theo dõi
    private <T> long countFollowedEntities(Long userId, Class<T> entityClass, String joinPath) {
        String entityAlias = entityClass.getSimpleName().substring(0, 1).toLowerCase();
        String countHql = String.format("SELECT COUNT(%s) FROM %s %s JOIN %s u WHERE u.id = :userId", entityAlias, entityClass.getSimpleName(), entityAlias, joinPath);
        TypedQuery<Long> countQuery = entityManager.createQuery(countHql, Long.class);
        countQuery.setParameter("userId", userId);
        return countQuery.getSingleResult();
    }


    // hàm tạo Page
    private <T> Page<T> createPage(List<T> content, PaginationRequest paginationRequest, long totalElements) {
        Pageable pageable = PageRequest.of(
                paginationRequest.getPage(),
                paginationRequest.getSize(),
                Sort.by(paginationRequest.getSortDirection(), paginationRequest.getSortBy())
        );
        return new PageImpl<>(content, pageable, totalElements);
    }
}