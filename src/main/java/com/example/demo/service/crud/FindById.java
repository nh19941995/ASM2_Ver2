package com.example.demo.service.crud;

import java.util.Optional;

public interface FindById <T, L> {
    Optional<T> findById(L id);
}
