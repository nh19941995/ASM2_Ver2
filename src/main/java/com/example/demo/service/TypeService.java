package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.Type;
import com.example.demo.entity.User;
import com.example.demo.service.crud.*;

public interface TypeService extends
        DeleteById<User, Long>,
        FindAll<Type>,
        FindById<Type, Long>,
        Save<Type>,
        Count
{
}
