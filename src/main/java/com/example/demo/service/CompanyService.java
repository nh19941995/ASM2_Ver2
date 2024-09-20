package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Company;
import com.example.demo.entity.User;
import com.example.demo.service.crud.*;
import com.example.demo.utility.PaginationRequest;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface CompanyService  extends
        Count,
        DeleteById<Company, Long>,
        FindAll<Company>,
        FindById<Company, Long>,
        Save<Company>
{
    Page<Company> findTop(PaginationRequest paginationRequest);
}
