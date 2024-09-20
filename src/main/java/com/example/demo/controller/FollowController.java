package com.example.demo.controller;

import com.example.demo.entity.Company;
import com.example.demo.entity.Recruitment;
import com.example.demo.service.UserService;
import com.example.demo.utility.CommonAttributesPopulator;
import com.example.demo.utility.PaginationRequest;
import com.example.demo.view.ViewConstants;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/follow")
public class FollowController {
    private final UserService userService;
    private final CommonAttributesPopulator commonAttributesPopulator;

    @Autowired
    public FollowController(UserService userService, CommonAttributesPopulator commonAttributesPopulator) {
        this.userService = userService;
        this.commonAttributesPopulator = commonAttributesPopulator;
    }

    // Phương thức được đánh dấu bằng @ModelAttribute sẽ được gọi
    // trước khi bất kỳ phương thức xử lý request nào
    // trong controller được thực thi.
    @ModelAttribute
    public void addCommonAttributes(Model model) {
        commonAttributesPopulator.userAuthenAndCompany(model);
    }


    // lấy danh sách công ty đã theo dõi
    // http://localhost:8080/follow/listFollowedCompanies?userId=1
    @GetMapping("/listFollowedCompanies")
    public String listFollowedCompanies(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model,
            HttpSession session
    ) {
        session.setAttribute("currentUrl", "/follow/listFollowedCompanies?userId=" + userId);
        Page<Company> companies = userService.findAllFollowedCompanies(
                userId, new PaginationRequest(page, size, "id", "asc"));
        model.addAttribute("saveJobList", companies);
        // lưu url hiện tại vào session
        String currentUrl = "/follow/listFollowedCompanies?userId=" + userId;
        session.setAttribute("currentUrl", currentUrl);
        return ViewConstants.LIST_FOLLOWED_COMPANIES;
    }

    // lấy danh sách recruiment đã theo dõi
    // http://localhost:8080/follow/listFollowedRecruiment?userId=1
    @GetMapping("/listFollowedRecruiment")
    public String listFollowedRecruiment(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model,
            HttpSession session
    ) {
        Page<Recruitment> recruitments = userService.findAllFollowedRecruiment(
                userId, new PaginationRequest(page, size, "id", "asc"));
        model.addAttribute("saveJobList", recruitments);
        // lưu url hiện tại vào session
        String currentUrl = "/follow/listFollowedRecruiment?userId=" + userId;
        session.setAttribute("currentUrl", currentUrl);
        return ViewConstants.LIST_FOLLOWED_POSTS;
    }


    // theo dõi công ty
    // http://localhost:8080/follow/followCompany?companyid=1&userid=1
    @GetMapping("/followCompany")
    public String follow(
            @RequestParam("companyId") Long companyId,
            @RequestParam("userId") Long userId,
            RedirectAttributes redirectAttributes,
            HttpSession session

    ) {
        userService.followCompany(userId, companyId);
        redirectAttributes.addFlashAttribute("messages", "Theo dõi công ty thành công !");
        // lấy url từ session
        String url = (String) session.getAttribute("currentUrl");
        return "redirect:" + url;
    }

    // bỏ theo dõi công ty
    // http://localhost:8080/follow/unFollowCompany?companyId=1&userId=1
    @GetMapping("/unFollowCompany")
    public String unFollow(
            @RequestParam("companyId") Long companyId,
            @RequestParam("userId") Long userId,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        userService.unfollowCompany(userId, companyId);
        redirectAttributes.addFlashAttribute("messages", "Bỏ theo dõi công ty thành công !");
        // lấy url từ session
        String url = (String) session.getAttribute("currentUrl");
        return "redirect:" + url;
    }

    // theo dõi recruiment
    // http://localhost:8080/follow/followRecruitment?recruitmentid=1&userid=1
    @GetMapping("/followRecruitment")
    public String followRecruitment(
            @RequestParam("recruitmentId") Long recruitmentid,
            @RequestParam("userId") Long userid,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        userService.followRecruiment(userid, recruitmentid);
        redirectAttributes.addFlashAttribute("messages", "Theo dõi bài đăng thành công !");
        // lấy url từ session
        String url = (String) session.getAttribute("currentUrl");
        return "redirect:" + url;
    }

    // bỏ theo dõi recruiment
    // http://localhost:8080/follow/unFollowRecruitment?recruitmentid=1&userid=1
    @GetMapping("/unFollowRecruitment")
    public String unFollowRecruitment(
            @RequestParam("recruitmentId") Long recruitmentId,
            @RequestParam("userId") Long userId,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        userService.unfollowfollowRecruiment(userId, recruitmentId);
        redirectAttributes.addFlashAttribute("messages", "Bỏ theo dõi bài đăng thành công !");
        // lấy url từ session
        String url = (String) session.getAttribute("currentUrl");
        return "redirect:" + url;
    }

}
