package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.service.crud.*;
import com.example.demo.utility.PaginationRequest;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.Set;

public interface UserService extends
        Count,
        DeleteById<User, Long>,
        FindAll<User>,
        FindById<User, Long>,
        Save<User>
{
    Optional<User> findByUsername(String username);
    Set<User> findUserApplyPost(Long companyId,int limit);
    Boolean checkToken(String token,String username);
    String generateToken(String username);

    public Page<Company> findAllFollowedCompanies(
            Long userId, PaginationRequest paginationRequest);
    public Page<Recruitment> findAllFollowedRecruiment(
            Long userId, PaginationRequest paginationRequest);

    public void followCompany(Long userId, Long companyId);
    public void unfollowCompany(Long userId, Long companyId);

    public void followRecruiment(Long userId, Long recruimentId);
    public void unfollowfollowRecruiment(Long userId, Long recruimentId);
    public void addDataUser(
             String fullName,
             String username,
             String password,
             String email,
             String address,
             String phone,
             String description,
             Long statusId,
             Long roleId
    );
}
