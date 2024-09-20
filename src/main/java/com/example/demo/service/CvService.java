package com.example.demo.service;

import com.example.demo.entity.Cv;
import com.example.demo.service.crud.*;

public interface CvService extends
        Count,
        DeleteById<Cv, Long>,
        FindAll<Cv>,
        FindById<Cv, Long>,
        Save<Cv> ,
        DeleteByEntity<Cv>
{
}
