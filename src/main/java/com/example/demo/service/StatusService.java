package com.example.demo.service;

import com.example.demo.entity.Status;
import com.example.demo.entity.User;
import com.example.demo.service.crud.*;

public interface StatusService extends
        Count,
        DeleteById<User , Long>,
        FindAll<Status>,
        FindById<Status, Long>,
        Save<Status>
{

}
