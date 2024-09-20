package com.example.demo.service.crud;

import com.example.demo.utility.PaginationRequest;
import org.springframework.data.domain.Page;

public interface FindAll<T> {
    Page<T> findAll(PaginationRequest paginationRequest);
    Iterable <T> findAll();
}
