package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.crud.*;

public interface RoleService  extends Count,
        DeleteById<User, Long>,
        FindAll<Role>,
        FindById<Role, Long>,
        Save<Role> {
}
