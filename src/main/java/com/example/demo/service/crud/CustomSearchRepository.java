package com.example.demo.service.crud;

import com.example.demo.utility.PaginationRequest;
import org.springframework.data.domain.Page;

public interface CustomSearchRepository<T> {
    Page<T> searchByField(
            String searchValue,
            String propertyName,
            PaginationRequest paginationRequest
    );
}
