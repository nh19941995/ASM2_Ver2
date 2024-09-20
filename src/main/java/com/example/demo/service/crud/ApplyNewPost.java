package com.example.demo.service.crud;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface ApplyNewPost<ApplyPost> {
    boolean applyNewPostOldCV(
            Long userId,
            Long recruitmentId,
            String message,
            RedirectAttributes redirectAttributes
    );

    boolean applyNewPostNewCV(
            Long userId,
            Long recruitmentId,
            String message,
            MultipartFile file,
            RedirectAttributes redirectAttributes
    );
}
