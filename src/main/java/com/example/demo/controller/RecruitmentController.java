package com.example.demo.controller;

import com.example.demo.entity.ApplyPost;
import com.example.demo.entity.Company;
import com.example.demo.entity.Recruitment;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.*;
import com.example.demo.utility.AddAttributesToModel;
import com.example.demo.utility.CommonAttributesPopulator;
import com.example.demo.utility.PaginationRequest;
import com.example.demo.utility.UserAccessChecker;
import com.example.demo.view.ViewConstants;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/recruitment")
public class RecruitmentController {
    private final RecruitmentService recruitmentService;
    private final UserAccessChecker userAccessChecker;
    private final UserService userService;
    private final AddAttributesToModel addAttributesToModel;
    private final CompanyService companyService;
    private final CategoryService categoryService;
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
    public RecruitmentController(
            RecruitmentService recruitmentService,
            UserAccessChecker userAccessChecker,
            UserService userService,
            AddAttributesToModel addAttributesToModel,
            CompanyService companyService,
            CategoryService categoryService,
            ApplyPostService applyPostService,
            CommonAttributesPopulator commonAttributesPopulator

    ) {
        this.recruitmentService = recruitmentService;
        this.userAccessChecker = userAccessChecker;
        this.userService = userService;
        this.addAttributesToModel = addAttributesToModel;
        this.companyService = companyService;
        this.categoryService = categoryService;
        this.applyPostService = applyPostService;
        this.commonAttributesPopulator = commonAttributesPopulator;
    }

    // lấy danh sách các bản ghi Recruitment theo companyId
    // http://localhost:8080/recruitment/all?size=2&page=1&sortBy=id&sortDirection=asc&companyId=5&userId=2
    @GetMapping("/allByCompany")
    public String allRecruitment(
            Model model,
            @RequestParam("companyId") Long companyId,
//            @RequestParam("userId") Long userId,
            // Thêm các thuộc tính phân trang
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
            // xác thực người dùng
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Kiểm tra xem người dùng có tồn tại và có quyền truy cập không
//        userAccessChecker.checkUserAccess(userId, userDetails);
        Page<Recruitment> recruitments = recruitmentService.findAllByCompanyId(
                new PaginationRequest(page, size, sortBy, sortDirection),
                companyId
        );

        model.addAttribute("recruitments", recruitments);
        // Thêm các thuộc tính vào model
        return ViewConstants.POST_LIST_VIEW;
    }


    // lấy danh sách các bản ghi Recruitment của toàn bộ các công ty
    // http://localhost:8080/recruitment/all?size=2&page=1&sortBy=id&sortDirection=asc&companyId=5&userId=2
    @GetMapping("/all")
    public String all(
            Model model,
            // Thêm các thuộc tính phân trang
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
            // xác thực người dùng
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Page<Recruitment> recruitments = recruitmentService.findAll(
                new PaginationRequest(page, size, sortBy, sortDirection)
        );

        model.addAttribute("recruitments", recruitments);
        // Thêm các thuộc tính vào model
        return ViewConstants.POST_LIST_VIEW;
    }





    // gọi form thêm mới Recruitment
    // http://localhost:8080/recruitment/addNew?userId=2
    @GetMapping("/addNew")
    public String addRecruiment(
            Model model,
            @RequestParam("userId") Long userId,
            @AuthenticationPrincipal UserDetails userDetails,

            RedirectAttributes redirectAttributes
    ) {
        // Kiểm tra xem người dùng có tồn tại và có quyền truy cập không
        userAccessChecker.checkUserAccess(userId, userDetails);
        // Thêm các thuộc tính vào model
        addAttributesToModel.addType(model, "types");
        addAttributesToModel.addCategory(model, "categories");
        model.addAttribute("recruitment", new Recruitment());

        // Thêm các thuộc tính vào model
        return ViewConstants.POST_JOB_VIEW;
    }

    // gọi form cập nhật Recruitment
    // http://localhost:8080/recruitment/update?recruitmentId=1&userId=2&companyId=5
    @GetMapping("/update")
    public String updateRecruitment(
            Model model,
            @RequestParam("recruitmentId") Long recruitmentId,
            @RequestParam("userId") Long userId,
            @RequestParam("companyId") Long companyId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Kiểm tra xem người dùng có tồn tại và có quyền truy cập không
        userAccessChecker.checkUserAccess(userId, userDetails);
        Recruitment recruitment = recruitmentService.findById(recruitmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruitment not found"));
        model.addAttribute("recruitment", recruitment);
        addAttributesToModel.addType(model, "types");
        addAttributesToModel.addCategory(model, "categories");
        return ViewConstants.POST_JOB_VIEW;
    }

    // thêm mới Recruitment hoặc cập nhật
    // http://localhost:8080/recruitment/post?userId=2&companyId=5
    @PostMapping("/post")
    public String postRecruitment(
            @Valid @ModelAttribute("recruitment") Recruitment recruitment,
            BindingResult bindingResult,
            Model model,
            @RequestParam("userId") Long userId,
            @RequestParam("companyId") Long companyId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes
    ) {
        // Kiểm tra xem người dùng có tồn tại và có quyền truy cập không
        userAccessChecker.checkUserAccess(userId, userDetails);

        if (bindingResult.hasErrors()) {
            addAttributesToModel.addType(model, "types");
            addAttributesToModel.addCategory(model, "categories");
            return ViewConstants.POST_JOB_VIEW;
        }
        Company company = new Company();
        company.setId(companyId);
        recruitment.setCompany(company);
        recruitmentService.save(recruitment);
        // Thông báo lên view
        redirectAttributes.addFlashAttribute("messages", "Yêu cầu thành công !");
        return "redirect:/recruitment/all?userId=" + userId + "&companyId=" + companyId;
    }

    // xóa Recruitment
    // http://localhost:8080/recruitment/delete?recruitmentId=1&userId=2
    @PostMapping("/delete")
    public String deleteRecruitment(
            @RequestParam("recruitmentId") Long recruitmentId,
            @RequestParam("userId") Long userId,
            @RequestParam("companyId") Long companyId,

            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Kiểm tra xem người dùng có tồn tại và có quyền truy cập không
        userAccessChecker.checkUserAccess(userId, userDetails);
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        recruitmentService.deleteById(user, recruitmentId);
        return "redirect:/recruitment/all?userId=" + userId+ "&companyId=" + companyId;
    }

    // chi tiết Recruitment
    // http://localhost:8080/recruitment/detail?recruitmentId=1&page=0&size=5
    @GetMapping("/detail")
    public String detailRecruitment(
            Model model,
            @RequestParam("recruitmentId") Long recruitmentId,
            // Thêm các thuộc tính phân trang
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            HttpSession session
    ) {
        // Lấy danh sách các bản ghi ApplyPost theo recruitmentId
        Page<ApplyPost> applyPosts = applyPostService.findAllByRecruitmentId(
                recruitmentId,
                new PaginationRequest(page, size, "id", "asc")
        );
        // Lấy bản ghi Recruitment theo recruitmentId
        Recruitment recruitment = recruitmentService.findById(recruitmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruitment not found"));
        // Thêm các thuộc tính vào model
        model.addAttribute("recruitment", recruitment);
        model.addAttribute("applyPosts", applyPosts);
        // lưu url hiện tại vào session
        String currentUrl = "/recruitment/detail?recruitmentId=" + recruitmentId;
        session.setAttribute("currentUrl", currentUrl);
        return ViewConstants.DETAIL_POST_VIEW;
    }







}
