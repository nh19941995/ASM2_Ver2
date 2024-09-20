package com.example.demo.controller;

import com.example.demo.config.EnumRole;
import com.example.demo.service.*;
import com.example.demo.service.email.EmailService;
import com.example.demo.view.ViewConstants;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import java.util.concurrent.CompletableFuture;

import static com.example.demo.security.MySecurityConfig.logger;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final RoleService roleService;
    private final StatusService statusService;
    private final EmailService emailService;

    @Autowired
    public AuthController(UserService userService, RoleService roleService, StatusService statusService, EmailService emailService) {
        this.userService = userService;
        this.roleService = roleService;
        this.statusService = statusService;
        this.emailService = emailService;
    }

    // trang đăng nhập
    // http://localhost:8080/auth/showMyLoginPage
    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "old/public/login";
    }


    // trang đăng ký
    // http://localhost:8080/auth/register
    @PostMapping("/register")
    public String showRegister(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            Model model,
            // dùng để truyền Attributes khi chuyển hướng
            RedirectAttributes redirectAttributes
    ) {
        // kiểm tra lỗi nhập liệu
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return "old/public/login";
        }
        // bắt đầu đăng ký
        try {
            // mã hóa mật khẩu
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Tạo đối tượng BCryptPasswordEncoder
            String encodedPassword = passwordEncoder.encode(user.getPassword()); // Mã hóa mật khẩu

            // Tùy chọn: Tạo chuỗi mã hóa theo định dạng {bcrypt}$2a$10$...
            String formattedEncodedPassword = String.format("{bcrypt}%s", encodedPassword);

            user.setPassword(formattedEncodedPassword); // Lưu mật khẩu đã mã hóa vào user
            userService.save(user);
            // nếu là manager thì gửi email xác thực
//            if (user.getRole().getRoleName().equals(EnumRole.MANAGER.getName())) {
//                // gửi email xác thực bất đồng bộ
//                CompletableFuture.runAsync(() -> {
//                    String token = userService.generateToken(user.getUsername());
//                    user.setToken(token);
//                    try {
//                        emailService.sendEmailAuthentication(
//                                user.getEmail(), user.getUsername(), token);
//                    } catch (MessagingException e) {
//                        logger.error("Không thể gửi email xác thực", e);
//                    }
//                });
//            }
            // truyền message cho trang tiếp theo
            redirectAttributes.addFlashAttribute("message", "Đăng ký thành công!");
            // chuyển hướng đến trang đăng nhập
            return "redirect:/auth/showMyLoginPage";
        } catch (Exception e) {
            user.setPassword(""); // xóa mật khẩu đã nhập
            model.addAttribute("error", "Có lỗi xảy ra khi đăng ký: " + e.getMessage());
            model.addAttribute("roles", roleService.findAll());
            return "old/public/login";
        }

    }

    // trang đăng xuất
    // http://localhost:8080/auth/logout
    @GetMapping("/logout")
    public String logout(
            HttpServletRequest request,
            HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        // Redirect về trang login với thông báo đã đăng xuất
        return "redirect:/";
    }

    // trang thông báo email chưa xác thực
    // http://localhost:8080/auth/verifycation
    @GetMapping("/verifycation")
    public String verifycation(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        return ViewConstants.EMAIL_INACTIVE;
    }

    //  xác thực email bằng token
    // http://localhost:8080/auth/verify?token=123&username=manager
    @GetMapping("/verify")
    public String verify(
            @RequestParam("token") String token,
            @RequestParam("username") String username

    ) {
        if (userService.checkToken(token, username)) {
            // thành công
            return "redirect:/auth/showMyLoginPage";
        }
        // thất bại
        return "redirect:/auth/verifycation";
    }

    // http://localhost:8080/auth/verifyAgain?username=manager
    @GetMapping("/verifyAgain")
    public String verifyAgain(
            @RequestParam("username") String username
    ) {
        // lấy thông tin user
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // gửi lại email xác thực bất đồng bộ
        CompletableFuture.runAsync(() -> {
            String token = userService.generateToken(user.getUsername());
            user.setToken(token);
            userService.save(user);
            try {
                emailService.sendEmailAuthentication(
                        user.getEmail(), user.getUsername(), token);
            } catch (MessagingException e) {
                logger.error("Không thể gửi email xác thực", e);
            }
        });
        return "redirect:/";
    }
}
