package com.example.demo.controller;

import com.example.demo.controller.dto.UserUpdate;
import com.example.demo.entity.Cv;
import com.example.demo.utility.AddAttributesToModel;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.*;
import com.example.demo.utility.FileStorageService;
import com.example.demo.utility.UserAccessChecker;
import com.example.demo.view.ViewConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;


@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final CvService cvService;
    private final RoleService roleService;
    private final FileStorageService fileStorageService;
    private final CompanyService companyService;
    private final AddAttributesToModel addAttributesToModel;
    private final UserAccessChecker userAccessChecker;

    @Autowired
    public UserController(UserService userService, CvService cvService, RoleService roleService, FileStorageService fileStorageService, CompanyService companyService, AddAttributesToModel addAttributesToModel, UserAccessChecker userAccessChecker) {
        this.userService = userService;
        this.cvService = cvService;
        this.roleService = roleService;
        this.fileStorageService = fileStorageService;
        this.companyService = companyService;
        this.addAttributesToModel = addAttributesToModel;
        this.userAccessChecker = userAccessChecker;
    }

    // gọi form update thông tin người dùng
    // http://localhost:8080/user/update?id=1
    @GetMapping("/update")
    public String getById(
            Model model,
            @RequestParam("id") Long userId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Kiểm tra xem người dùng có tồn tại và có quyền truy cập không
        userAccessChecker.checkUserAccess(userId, userDetails);

        // Thêm các thuộc tính vào model
        addAttributesToModel.addUserDtoById(model, userId,"user");
        addAttributesToModel.addCv(model, userId, cvService);
        addAttributesToModel.addCompany(model, userId, companyService, "company");
        addAttributesToModel.addRole(model, userId, roleService);
        addAttributesToModel.addAvatar(model, userId);

        // Trả về view
        return ViewConstants.PROFILE_VIEW;
    }

    // cập nhật thông tin người dùng
    // http://localhost:8080/user/updateUser
    @PostMapping("/updateUser")
    public String updateUser(@Valid @ModelAttribute("user") UserUpdate userDto,
                             BindingResult bindingResult,
                             @RequestParam("file") MultipartFile file,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Kiểm tra xem người dùng có tồn tại và có quyền truy cập không
        userAccessChecker.checkUserAccess(userDto.getId(), userDetails);

        // validate userDto
        if (bindingResult.hasErrors()) {
            addAttributesToModel.addCv(model, userDto.getId(), cvService);
            addAttributesToModel.addCompany(model, userDto.getId(), companyService, "company");
            addAttributesToModel.addRole(model, userDto.getId(), roleService);
            addAttributesToModel.addAvatar(model, userDto.getId());
            return ViewConstants.PROFILE_VIEW;
        }

        // lưu user trước khi lưu file CV
        User newUser = new User(userDto);
        User user = userService.save(newUser);  // Lưu user và lấy user đã được lưu

        // Lưu file cv
        boolean saved = fileStorageService.saveCv(file, user, redirectAttributes);
        // Thông báo cập nhật thành công
        if (saved) redirectAttributes.addFlashAttribute("messages", "Cập nhật thông tin người dùng thành công !");
        // Chuyển hướng về trang cập nhật
        return "redirect:/user/update?id=" + userDto.getId();
    }

    // cập nhật ảnh đại diện
    // http://localhost:8080/user/addAvatar
    @PostMapping("/addAvatar")
    public String addAvatar(@RequestParam("file") MultipartFile file,
                            @RequestParam("id") Long userId,
                            RedirectAttributes redirectAttributes,
                            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Kiểm tra xem người dùng có tồn tại và có quyền truy cập không
        userAccessChecker.checkUserAccess(userId, userDetails);

        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // Lưu file ảnh đại diện
        Boolean saveAvatar = fileStorageService.saveAvatar(file, user, redirectAttributes);
        // Thông báo cập nhật thành công
        if (saveAvatar)redirectAttributes.addFlashAttribute("messages", "Cập nhật ảnh đại diện thành công !");
        // Chuyển hướng về trang cập nhật
        return "redirect:/user/update?id=" + userId;
    }

    // xóa cv
    // http://localhost:8080/user/delete/cv?id=1&userId=2
    @PostMapping("/delete/cv")
    public String deleteCv(
            @RequestParam("id") Long cvId,
            @RequestParam("userId") Long userId
    ) {
        // câp nhật db
        Cv cv =  cvService.findById(cvId)
                .orElseThrow(() -> new ResourceNotFoundException("CV not found"));
        cvService.deleteByEntity(cv);
        // xóa file
        fileStorageService.deleteCvFile(cv.getFileName());
        // chuyển hướng
        return "redirect:/user/update?id=" + userId;
    }

    // lấy ra các user đã apply vào bài đăng của công ty mình
    // http://localhost:8080/user/delete/avatar?id=1
    @GetMapping("/getAllapply")
    public String getAll(
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Set<User> users = userService.findUserApplyPost(user.getCompany().getId(), 10);

        model.addAttribute("users", users);

        return ViewConstants.LIST_USER_VIEW;
    }


}
