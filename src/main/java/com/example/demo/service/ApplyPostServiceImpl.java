package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ApplyPostRepo;
import com.example.demo.utility.FileStorageService;
import com.example.demo.utility.PaginationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ApplyPostServiceImpl implements ApplyPostService{

    private final ApplyPostRepo applyPostRepo;
    private final RecruitmentService recruitmentService;
    private final UserService userService;
    private final StatusService statusService;
    private final FileStorageService fileStorageService;

    @Autowired
    public ApplyPostServiceImpl(
            ApplyPostRepo applyPostRepo,
            RecruitmentService recruitmentService,
            UserService userService,
            StatusService statusService,
            FileStorageService fileStorageService
    ) {
        this.applyPostRepo = applyPostRepo;
        this.recruitmentService = recruitmentService;
        this.userService = userService;
        this.statusService = statusService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public int count() {
        return (int)applyPostRepo.count();
    }

    @Override
    public void deleteById(ApplyPost applyPost, Long id) {
        applyPostRepo.deleteById(id);
    }

    @Override
    public Page<ApplyPost> findAll(PaginationRequest paginationRequest) {
        Page<ApplyPost> companyPage = applyPostRepo.findAll(paginationRequest.toPageable());

        if (paginationRequest.getPage() >= companyPage.getTotalPages()) {
            throw new ResourceNotFoundException("The requested page does not exist");
        }
        return companyPage;
    }

    @Override
    public Iterable<ApplyPost> findAll() {
        return applyPostRepo.findAll();
    }

    @Override
    public Optional<ApplyPost> findById(Long id) {
        return applyPostRepo.findById(id);
    }

    @Override
    public ApplyPost save(ApplyPost applyPost) {
        return applyPostRepo.save(applyPost);
    }


    @Override
    public boolean applyNewPostOldCV(Long userId, Long recruitmentId, String message,RedirectAttributes redirectAttributes) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));
        // kiểm tra xem user đã ứng tuyển chưa
        Optional<ApplyPost> existApplyPost = applyPostRepo.checkExistApplyPost(userId, recruitmentId);
        if (existApplyPost.isPresent()) {
            redirectAttributes.addFlashAttribute("messages", "Bạn đã ứng tuyển bài đăng này rồi");
            return false;
        }

        Recruitment recruitment = recruitmentService.findById(recruitmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài đăng"));
        Cv cv = user.getCv();
        if (cv == null) {
            redirectAttributes.addFlashAttribute("messages", "Bạn chưa tạo CV");
            return false;
        }
        Status status = statusService.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy status"));
        // nâng rank cho category
        Category category = recruitment.getCategory();
        category.setNumberChoose(category.getNumberChoose() + 1);
        // tạo applyPost
        ApplyPost applyPost = ApplyPost.builder()
                .cv(cv)
                .recruitment(recruitment)
                .text(message)
                .status(status)
                .createdDate(LocalDate.now())
                .build();
        // lưu applyPost
        applyPostRepo.save(applyPost);
        return true;
    }

    @Override
    public boolean applyNewPostNewCV(
            Long userId,
            Long recruitmentId,
            String message,
            MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {

        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        boolean saveCv = fileStorageService.saveCv(file, user, redirectAttributes);

        if (!saveCv) {
            return false;
        }

        return applyNewPostOldCV(userId, recruitmentId, message, redirectAttributes);
    }


    @Override
    public Page<ApplyPost> findAllByCompanyId(Long userId, Long companyId, PaginationRequest paginationRequest) {
        return applyPostRepo.findAllByComanyId(companyId, paginationRequest);
    }

    @Override
    public Page<ApplyPost> findAllByRecruitmentId(Long recruitmentId, PaginationRequest paginationRequest) {
        return applyPostRepo.findAllByRecruitmentId(recruitmentId, paginationRequest);
    }

    @Override
    public Page<ApplyPost> findAllAppliedPostsByUser(Long userId, PaginationRequest paginationRequest) {
        return applyPostRepo.findAllAppliedPostsByUser(userId,paginationRequest);
    }

    @Override
    public boolean removeApply(Long userId, Long recruitmentId) {
        ApplyPost applyPost = applyPostRepo.checkExistApplyPost(userId, recruitmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ ứng tuyển"));
        applyPostRepo.delete(applyPost);
        return true;
    }
}
