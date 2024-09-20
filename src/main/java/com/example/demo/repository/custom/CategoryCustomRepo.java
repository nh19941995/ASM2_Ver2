package com.example.demo.repository.custom;
import com.example.demo.entity.Category;

import java.util.List;

public interface CategoryCustomRepo {
    public List<Category> findTopList(int days);
    public List<Category> findTopList();

}
