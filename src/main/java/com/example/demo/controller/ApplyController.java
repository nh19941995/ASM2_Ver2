package com.example.demo.controller;

import com.example.demo.entity.ApplyPost;
import com.example.demo.service.ApplyPostService;
import com.example.demo.utility.CommonAttributesPopulator;
import com.example.demo.utility.PaginationRequest;
import com.example.demo.view.ViewConstants;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/apply")
public class ApplyController {

    private final ApplyPostService applyPostService;
    private final CommonAttributesPopulator commonAttributesPopulator;

    // Phương thức được đánh dấu bằng @ModelAttribute sẽ được gọi
    // trước khi bất kỳ phương thức xử lý request nào
    // trong controller được thực thi.
    @ModelAttribute
    public void addCommonAttributes(Model model) {
        commonAttributesPopulator.userAuthenAndCompany(model);
    }

    @Autowired
    public ApplyController(ApplyPostService applyPostService, CommonAttributesPopulator commonAttributesPopulator) {
        this.applyPostService = applyPostService;
        this.commonAttributesPopulator = commonAttributesPopulator;
    }


    // lấy ra toàn bộ bài đăng mà công ty của manager đã đăng
    // http://localhost:8080/apply/all?size=2&page=1&sortBy=id&sortDirection=asc&companyId=5&userId=2
    @GetMapping("/all")
    public String showAllApplyPost(
            Model model,
            @RequestParam("companyId") Long companyId,
            @RequestParam("userId") Long userId,
            // Thêm các thuộc tính phân trang
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
            HttpSession session
    ) {
        PaginationRequest paginationRequest = new PaginationRequest(page, size, sortBy, sortDirection);
        Page<ApplyPost> applyPosts = applyPostService.findAllByCompanyId(userId, companyId, paginationRequest);

        model.addAttribute("applyPosts", applyPosts);
        // lưu url hiện tại vào session
        String currentUrl = "/apply/all?size=" + size + "&page=" + page + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection + "&companyId=" + companyId + "&userId=" + userId;
        session.setAttribute("currentUrl", currentUrl);
        return ViewConstants.LIST_USER;
    }

    // lấy ra toàn bộ bài đăng mà user đã ứng tuyển
    // http://localhost:8080/apply/allPostApply?userId=2
    @GetMapping("/allPostApply")
    public String showAllPostApply(
            Model model,
            @RequestParam("userId") Long userId,
            // Thêm các thuộc tính phân trang
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
            HttpSession session
    ) {
        PaginationRequest paginationRequest = new PaginationRequest(page, size, sortBy, sortDirection);
        Page<ApplyPost> applyPosts = applyPostService.findAllAppliedPostsByUser(userId, paginationRequest);

        model.addAttribute("appliedPosts", applyPosts);
        // lưu url hiện tại vào session
        String currentUrl = "/apply/allPostApply?size=" + size + "&page=" + page + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection + "&userId=" + userId;
        session.setAttribute("currentUrl", currentUrl);
        return ViewConstants.LIST_APPLY_JOB;
    }

    // ứng tuyển bài đăng với CV cũ
    // http://localhost:8080/apply/applyWithOldCv?recruitmentId=1&userId=2&message=Hello
    @PostMapping("/applyWithOldCv")
    public String applyWithOldCv(
            @RequestParam("recruitmentId") Long postId,
            @RequestParam("userId") Long userId,
            @RequestParam("message") String message,
            RedirectAttributes redirectAttributes
    ) {
        boolean result = applyPostService.applyNewPostOldCV(userId, postId,message,redirectAttributes);
        if (result)redirectAttributes.addFlashAttribute("messages", "Ứng tuyển thành công");
        return "redirect:/";
    }

    // ứng tuyển bài đăng với CV mới
    // http://localhost:8080/apply/applyWithNewCv?recruitmentId=1&userId=2&message=Hello
    @PostMapping("/applyWithNewCv")
    public String applyWithNewCv(
            @RequestParam("recruitmentId") Long recruitmentId,
            @RequestParam("userId") Long userId,
            @RequestParam("message") String message,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        boolean result =  applyPostService.applyNewPostNewCV(userId, recruitmentId, message, file,redirectAttributes);
        if (result)redirectAttributes.addFlashAttribute("messages", "Ứng tuyển thành công");
        // lấy url từ session
        String url = (String) session.getAttribute("currentUrl");
        return "redirect:" + url;
    }


    // hủy ứng tuyển
    // http://localhost:8080/apply/removeApply?recruitmentId=1&userId=2
    @GetMapping("/removeApply")
    public String removeApply(
            @RequestParam("recruitmentId") Long postId,
            @RequestParam("userId") Long userId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        boolean result = applyPostService.removeApply(userId, postId);
        if (result)redirectAttributes.addFlashAttribute("messages", "Xóa ứng tuyển thành công !");

        // lấy url từ session
        String url = (String) session.getAttribute("currentUrl");
        return "redirect:" + url;
    }

}
