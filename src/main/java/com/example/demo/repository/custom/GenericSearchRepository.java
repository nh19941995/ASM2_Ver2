package com.example.demo.repository.custom;

import java.util.List;

public interface GenericSearchRepository<T, ID> {
    List<T>  customSearchRepository(String searchTerm, String fieldName,int maxResults);
}
