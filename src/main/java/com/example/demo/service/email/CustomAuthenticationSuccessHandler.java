package com.example.demo.service.email;

import com.example.demo.config.EnumRole;
import com.example.demo.config.EnumStatus;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.example.demo.security.MySecurityConfig.logger;

// Xử lý sau khi người dùng đăng nhập thành công
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        String username = authentication.getName();
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean isInactive = user.getStatus().getStatusName().equals(EnumStatus.INACTIVE.getName());
            // nếu chưa xác thực email (chỉ áp dụng cho manager)
            if (isInactive) {
                // Đăng xuất người dùng ngay lập tức
                request.getSession().invalidate();
                // Xóa thông tin xác thực của người dùng
                SecurityContextHolder.clearContext();
                // lưu username vào session
                request.getSession().setAttribute("username", username);
                // Chuyển hướng đến trang xác thực
                response.sendRedirect("/auth/verifycation");
            } else {
                // nếu manager đăng nhập thì gửi email xác nhận đăng nhập
                if (user.getRole().getRoleName().equals(EnumRole.MANAGER.getName())) {
                    // Gửi email xác nhận đăng nhập bất đồng bộ
                    CompletableFuture.runAsync(() -> {
                        try {
                            Date loginTime = new Date();
                            emailService.sendLoginNotification(
                                    user.getEmail(),
                                    user.getUsername(),
                                    loginTime
                            );
                        } catch (MessagingException e) {
                            logger.error("Không thể gửi email xác nhận đăng nhập", e);
                        }
                    });
                }

                // Chuyển hướng đến trang chủ sau khi đăng nhập thành công
                response.sendRedirect("/");
            }
        } else {
            throw new RuntimeException("Không tìm thấy thông tin người dùng");
        }
    }
}