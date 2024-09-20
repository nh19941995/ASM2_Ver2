package com.example.demo.repository.custom;

import com.example.demo.entity.Company;
import com.example.demo.entity.Recruitment;
import com.example.demo.utility.PaginationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface CompanyCustomRepo {
    // công ty có tổng số lượng tuyển dụng nhiều nhất trong 1 năm
    Page<Company> findTop(Pageable pageable);
}
