package com.example.demo.service;

import com.example.demo.config.EnumRole;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RoleRepo;
import com.example.demo.repository.StatusRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.email.EmailService;
import com.example.demo.utility.PaginationRequest;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.example.demo.security.MySecurityConfig.logger;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepository;
    private final RoleRepo roleRepository;
    private final StatusRepo statusRepository;
    private final EmailService emailService;
    private final CompanyService companyService;
    private final RecruitmentService recruitmentService;

    @Autowired
    public UserServiceImpl(UserRepo userRepository, RoleRepo roleRepository, StatusRepo statusRepository, EmailService emailService, CompanyService companyService, RecruitmentService recruitmentService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
        this.emailService = emailService;
        this.companyService = companyService;
        this.recruitmentService = recruitmentService;
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User save(User user) {
        boolean isEmailExisting = userRepository.checkExistingPropty("email", user.getEmail());

        // Kiểm tra nếu là cập nhật user
        if (user.getId() != null) {
            User old = userRepository.findById(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            // Các trường không được cập nhật
            user.setPassword(old.getPassword());
            user.setUsername(old.getUsername());
            user.setRole(old.getRole());
            user.setStatus(old.getStatus());
            user.setImage(old.getImage());
            user.setCv(old.getCv());
        }

        // Trường hợp tạo mới
        if (user.getId() == null) {

            if (isEmailExisting) {
                throw new DuplicateKeyException("Email đã tồn tại trong hệ thống");
            }

            boolean isUsernameExisting = userRepository.checkExistingPropty("username", user.getUsername());
            if (isUsernameExisting) {
                throw new DuplicateKeyException ("Username đã tồn tại trong hệ thống");
            }

            boolean isPhoneExisting = userRepository.checkExistingPropty("phone", user.getPhone());
            if (isPhoneExisting) {
                throw new DuplicateKeyException("Số điện thoại đã tồn tại trong hệ thống");
            }

            Role role = roleRepository.findById(user.getRole().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

            // Nếu là tài khoản MANAGER
            if (role.getRoleName().equals(EnumRole.MANAGER.getName())) {
                // Set status INACTIVE nếu role là MANAGER
                user.setStatus(statusRepository.findById(3L).orElseThrow
                        (() -> new ResourceNotFoundException("Status not found")));
                // gui email xac thuc
                CompletableFuture.runAsync(() -> {
                    String token = this.generateToken(user.getUsername());
                    user.setToken(token);
                    try {
                        emailService.sendEmailAuthentication(
                                user.getEmail(), user.getUsername(),token);
                    } catch (MessagingException e) {
                        logger.error("Không thể gửi email xác thực", e);
                    }
                });

            } else {
                // Set status ACTIVE cho các tài khoản khác
                user.setStatus(statusRepository.findById(1L).orElseThrow
                        (() -> new ResourceNotFoundException("Status not found")));
            }
            user.setRole(role);
        }

        // Lưu user vào database
        return userRepository.save(user);
    }


    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Page<User> findAll(PaginationRequest paginationRequest) {
        // chỉ lấy ra các user đã cập nhật cv và chưa bị block
        Page<User> userPage = userRepository
                .findAllByStatusIdLessThanAndCvIdGreaterThan(2L, 0L, paginationRequest.toPageable());

        if (paginationRequest.getPage() >= userPage.getTotalPages()) {
            throw new ResourceNotFoundException("The requested page does not exist");
        }
        return userPage;
    }

    @Override
    public int count() {
        return (int) userRepository.count();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Set<User> findUserApplyPost(Long companyId,int limit) {
        return userRepository.findUserApplyPost(companyId,limit);
    }

    @Override
    public Boolean checkToken(String token, String username) {
        // Tìm User dựa trên username, nếu không tìm thấy sẽ ném ngoại lệ
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Sử dụng Objects.equals() trong Java để so sánh hai đối tượng
        //  trường hợp một hoặc cả hai đối tượng có thể là null.
        if (Objects.equals(user.getToken(), token)) {
            // Cập nhật trạng thái của user
            user.setStatus(statusRepository.findById(1L)
                    .orElseThrow(() -> new ResourceNotFoundException("Status not found")));

            // Lưu lại user với trạng thái mới
            userRepository.save(user);
            return true;
        }
        return false; // Token không khớp
    }

    @Transactional
    @Override
    public String generateToken(String username) {
        return username + System.currentTimeMillis();
    }

    @Override
    public Page<Company> findAllFollowedCompanies(Long userId, PaginationRequest paginationRequest) {
        return userRepository.findAllFollowedCompanies(userId, paginationRequest);
    }

    @Override
    public Page<Recruitment> findAllFollowedRecruiment(Long userId, PaginationRequest paginationRequest) {
        return userRepository.findAllFollowedRecruiment(userId, paginationRequest);
    }

    @Transactional
    @Override
    public void followCompany(Long userId, Long companyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Company company = companyService.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        user.followNewCompany(company);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void unfollowCompany(Long userId, Long companyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Company company = companyService.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        user.removeFollowCompany(company);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void followRecruiment(Long userId, Long recruimentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Recruitment recruitment = recruitmentService.findById(recruimentId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruitment not found"));
        user.followNewRecruitment(recruitment);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void unfollowfollowRecruiment(Long userId, Long recruimentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Recruitment recruitment = recruitmentService.findById(recruimentId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruitment not found"));
        user.removeFollowRecruitment(recruitment);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void addDataUser(String fullName, String username, String password, String email, String address, String phone, String description, Long statusId, Long roleId) {
        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setAddress(address);
        user.setPhone(phone);
        user.setDescription(description);
        user.setStatus(statusRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found")));
        user.setRole(roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found")));
        // mã hóa mật khẩu
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Tạo đối tượng BCryptPasswordEncoder
        String encodedPassword = passwordEncoder.encode(password); // Mã hóa mật khẩu

        // Tùy chọn: Tạo chuỗi mã hóa theo định dạng {bcrypt}$2a$10$...
        String formattedEncodedPassword = String.format("{bcrypt}%s", encodedPassword);

        user.setPassword(formattedEncodedPassword); // Lưu mật khẩu đã mã hóa vào user
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteById(User user, Long id) {
        userRepository.deleteById(id);
    }
}
