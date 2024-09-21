package com.example.demo.repository;
import com.example.demo.entity.Recruitment;
import com.example.demo.repository.custom.GenericSearchRepository;
import com.example.demo.repository.custom.RecruitmentCustomRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepo extends
        JpaRepository<Recruitment, Long>,
        RecruitmentCustomRepo,
        GenericSearchRepository<Recruitment, Long>
{
    Page<Recruitment> findAllByStatusIdLessThan(Long status_id, Pageable pageable);
}
