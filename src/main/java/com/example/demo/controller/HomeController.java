package com.example.demo.controller;

import com.example.demo.controller.dto.UserUpdate;
import com.example.demo.entity.Category;
import com.example.demo.entity.Company;
import com.example.demo.entity.Recruitment;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.*;
import com.example.demo.utility.AddAttributesToModel;
import com.example.demo.utility.CommonAttributesPopulator;
import com.example.demo.utility.PaginationRequest;
import com.example.demo.view.ViewConstants;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class HomeController {
    private final CategoryService categoryService;
    private final RecruitmentService recruitmentService;
    private final CommonAttributesPopulator commonAttributesPopulator;
    private final UserService userService;

    // Phương thức được đánh dấu bằng @ModelAttribute sẽ được gọi
    // trước khi bất kỳ phương thức xử lý request nào
    // trong controller được thực thi.
    @ModelAttribute
    public void addCommonAttributes(Model model) {
        commonAttributesPopulator.userAuthenAndCompany(model);
        commonAttributesPopulator.addBestRecruiment(model);
    }

    @Autowired
    public HomeController(CategoryService categoryService, RecruitmentService recruitmentService, CommonAttributesPopulator commonAttributesPopulator, UserService userService) {
        this.categoryService = categoryService;
        this.recruitmentService = recruitmentService;
        this.commonAttributesPopulator = commonAttributesPopulator;
        this.userService = userService;
    }

    // trang chủ
    // http://localhost:8080/
    @GetMapping("")
    public String showHome(Model model,HttpSession session) {
        model.addAttribute("categories", categoryService.findTopList());
        model.addAttribute("bestPost", recruitmentService
                .bestPost(new PaginationRequest(0, 1, "id", "asc")));
        // lưu url hiện tại vào session
        session.setAttribute("currentUrl", "/");

        return "old/public/home";
    }

    @GetMapping("/bestPost")
    public String bestPost(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "1") int size,
            HttpSession session
    ) {
        model.addAttribute("categories", categoryService.findTopList());
        model.addAttribute("bestPost", recruitmentService
                .bestPost(new PaginationRequest(page, size, "id", "asc")));
        // lưu url hiện tại vào session
        String currentUrl = "/bestPost?page=" + page + "&size=" + size;
        session.setAttribute("currentUrl", currentUrl);

        return "old/public/home";
    }


    // tìm kiếm theo tiêu đề recruitment
    // http://localhost:8080/search?searchTerm=java&page=0&size=5&sortBy=id&sortDirection=asc
    @GetMapping("/searchRecruitmentTitle")
    public String searchRecruitment(
            Model model,
            @RequestParam("searchTerm") String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
            HttpSession session
    ) {
        Page<Recruitment> recruitmentMatches = recruitmentService.searchByField(
                searchTerm,
                "title",
                new PaginationRequest(page, size, sortBy, sortDirection)
        );
        model.addAttribute("categories", categoryService.findTopList());
        model.addAttribute("recruitmentMatches", recruitmentMatches);

        // url dùng cho phân trang
        model.addAttribute("url", "/searchRecruitmentTitle");
        // tạo biến sessionTab để xác định tab hiện tại
        session.setAttribute("sessionTab", "recruitmentTitle");
        // lưu url hiện tại vào session
        String currentUrl = "/searchRecruitmentTitle?searchTerm=" + searchTerm + "&page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection;
        session.setAttribute("currentUrl", currentUrl);
        return ViewConstants.HOME;
    }

    // tìm kiếm công ty theo tên công ty
    // http://localhost:8080/searchCompanyName?searchTerm=abc&page=0&size=5&sortBy=id&sortDirection=asc
    @GetMapping("/searchCompanyName")
    public String searchCompany(
            Model model,
            @RequestParam("searchTerm") String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
            HttpSession session
    ) {
        Page<Recruitment> recruitmentMatches = recruitmentService.searchByField(
                searchTerm,
                "company.nameCompany",
                new PaginationRequest(page, size, sortBy, sortDirection)
        );

        model.addAttribute("categories", categoryService.findTopList());
        model.addAttribute("recruitmentMatches", recruitmentMatches);
        // url dùng cho phân trang
        model.addAttribute("url", "/searchCompanyName");
        // tạo biến sessionTab để xác định tab hiện tại
        session.setAttribute("sessionTab", "companyName");
        // lưu url hiện tại vào session
        String currentUrl = "/searchCompanyName?searchTerm=" + searchTerm + "&page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection;
        session.setAttribute("currentUrl", currentUrl);
        return ViewConstants.HOME;
    }


    // tìm kiếm công ty theo địa chỉ công ty
    // http://localhost:8080/searchCompanyLocation?searchTerm=abc&page=0&size=5&sortBy=id&sortDirection=asc
    @GetMapping("/searchCompanyLocation")
    public String searchCompanyLocation(
            Model model,
            @RequestParam("searchTerm") String searchTerm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
            HttpSession session
    ) {
        Page<Recruitment> recruitmentMatches = recruitmentService.searchByField(
                searchTerm,
                "address",
                new PaginationRequest(page, size, sortBy, sortDirection)
        );

        model.addAttribute("categories", categoryService.findTopList());
        model.addAttribute("recruitmentMatches", recruitmentMatches);
        // url dùng cho phân trang
        model.addAttribute("url", "/searchCompanyLocation");
        // tạo biến sessionTab để xác định tab hiện tại
        session.setAttribute("sessionTab", "companyLocation");
        // lưu url hiện tại vào session
        String currentUrl = "/searchCompanyLocation?searchTerm=" + searchTerm + "&page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&sortDirection=" + sortDirection;
        session.setAttribute("currentUrl", currentUrl);
        return ViewConstants.HOME;
    }


    @GetMapping("/allUser")
    public String getAllUser(
            Model model,
            // Thêm các thuộc tính phân trang
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
            HttpSession session
    ) {
        Page<User> users = userService.findAll(
                new PaginationRequest(page, size, sortBy, sortDirection));
        model.addAttribute("users", users);
        // lưu url hiện tại vào session
        String currentUrl = "/recruitment/all";
        session.setAttribute("users", currentUrl);

        return ViewConstants.LIST_ALL_USER;
    }


}