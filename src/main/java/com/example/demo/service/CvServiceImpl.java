package com.example.demo.service;

import com.example.demo.entity.Company;
import com.example.demo.entity.Cv;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CvRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.utility.PaginationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CvServiceImpl implements CvService {

    private final CvRepo cvRepo;

    @Autowired
    public CvServiceImpl( CvRepo cvRepo) {
        this.cvRepo = cvRepo;
    }

    @Override
    public Iterable<Cv> findAll() {
        return cvRepo.findAll();
    }

    @Override
    public Cv save(Cv cv) {
        return cvRepo.save(cv);
    }

    @Override
    public Optional<Cv> findById(Long id) {
        return cvRepo.findById(id);
    }

    @Override
    public Page<Cv> findAll(PaginationRequest paginationRequest) {
        Page<Cv> cvPage = cvRepo.findAll(paginationRequest.toPageable());

        if (paginationRequest.getPage() >= cvPage.getTotalPages()) {
            throw new ResourceNotFoundException("The requested page does not exist");
        }
        return cvPage;
    }

    @Override
    public int count() {
        return (int) cvRepo.count();
    }

    @Override
    public void deleteByEntity(Cv cv) {
        cv.getUser().removeCv();
        cvRepo.delete(cv);
    }


    @Override
    public void deleteById(Cv cv, Long id) {
        cvRepo.deleteById(id);
    }

}
