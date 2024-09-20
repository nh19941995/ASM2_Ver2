package com.example.demo.service.crud;

import com.example.demo.entity.User;

public interface DeleteById<T,L> {
    void deleteById( T t,L id);
}
