package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.service.crud.*;

import java.util.List;

public interface CategoryService extends
        Count,
        DeleteById<Category, Long>,
        FindAll<Category>,
        FindById<Category, Long>,
        Save<Category>
{
    public List<Category> findTopList(int days);
    public List<Category> findTopList();
}
