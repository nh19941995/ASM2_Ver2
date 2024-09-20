package com.example.demo.utility;

import com.example.demo.entity.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserAccessChecker {

    private final UserService userService;

    @Autowired
    public UserAccessChecker(UserService userService) {
        this.userService = userService;
    }


    public void checkUserAccess(Long userId, UserDetails userDetails) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.getUsername().equals(userDetails.getUsername())) {
            throw new BadRequestException("You don't have permission to access this page");
        }
    }
}
