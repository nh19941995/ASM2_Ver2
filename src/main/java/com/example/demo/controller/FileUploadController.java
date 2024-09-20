package com.example.demo.controller;

import com.example.demo.entity.Company;
import com.example.demo.entity.Cv;
import com.example.demo.entity.User;
import com.example.demo.service.CompanyServiceImpl;
import com.example.demo.service.CvServiceImpl;
import com.example.demo.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class FileUploadController {
    private final UserServiceImpl userServiceImpl;
    private final CvServiceImpl cvServiceImpl;
    private final CompanyServiceImpl companyServiceImpl;
    private final Environment env;

    @Autowired
    public FileUploadController(UserServiceImpl userServiceImpl, CvServiceImpl cvServiceImpl, CompanyServiceImpl companyServiceImpl, Environment env) {
        this.userServiceImpl = userServiceImpl;
        this.cvServiceImpl = cvServiceImpl;
        this.companyServiceImpl = companyServiceImpl;
        this.env = env;
    }

    // Upload file CV
    // http://localhost:8080/upload
    @PostMapping("/upload")
    public String handleFileUpload(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes,
            @RequestParam("id") int id
    ) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng chọn một file để upload.");
            return "redirect:/user/update?id=" + id;
        }

        try {
            // Lấy đường dẫn từ application.properties
            String uploadDir = env.getProperty("file.upload-dir-cv", "./uploads/cv/");

            // Tạo thư mục nếu chưa tồn tại
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Tạo tên file
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = StringUtils.getFilenameExtension(originalFilename);

            Optional<User> userOptional = userServiceImpl.findById(Long.parseLong(String.valueOf(id)));
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String uniqueFilename = user.getUsername() + "." + fileExtension;

                // Kiểm tra loại file
                if (fileExtension == null || !fileExtension.equalsIgnoreCase("pdf")) {
                    redirectAttributes.addFlashAttribute("message", "Chỉ chấp nhận file PDF.");
                    return "redirect:/user/update?id=" + id;
                }

                // đường dẫn file
                Path filePath = uploadPath.resolve(uniqueFilename);
                Cv cv = user.getCv();

                // Nếu user chưa có cv thì tạo mới
                if (cv == null) {
                    cv = new Cv();
                    user.setCv(cv);
                    cvServiceImpl.save(cv);
                }

                // Nếu user đã có cv thì cập nhật
                cv.setFileName(uniqueFilename);
                userServiceImpl.save(user);

                // Copy file vào thư mục upload, nếu file đã tồn tại thì thay thế
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                redirectAttributes.addFlashAttribute("message",
                        "Bạn đã upload thành công file '" + uniqueFilename + "'!");
            } else {
                redirectAttributes.addFlashAttribute("message", "User không tồn tại.");
                return "redirect:/user/update?id=" + id;
            }

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message",
                    "Upload thất bại: " + e.getMessage());
        }

        return "redirect:/user/update?id=" + id;
    }

    // Upload file logo công ty
    // http://localhost:8080/uploadCompanyLogo
    @PostMapping("/uploadCompanyLogo")
    public String handleFileUploadCompanyLogo(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes,
            @RequestParam("id") int idUser
    ) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng chọn một file để upload.");
            return "redirect:/company/update?id=" + idUser;
        }

        try {
            // Lấy đường dẫn từ application.properties
            String uploadDir = env.getProperty("file.upload-dir-logo", "./uploads/logo/");

            // Tạo thư mục nếu chưa tồn tại
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Tạo tên file duy nhất
            String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String fileExtension = StringUtils.getFilenameExtension(originalFilename);

            Optional<User> userOptional = userServiceImpl.findById(Long.parseLong(String.valueOf(idUser)));
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String uniqueFilename = user.getUsername() + "." + fileExtension;

                // Kiểm tra loại file
                List<String> acceptedExtensions = Arrays.asList("png", "svg", "jpeg", "ai", "eps");

                if (fileExtension == null || !acceptedExtensions.contains(fileExtension.toLowerCase())) {
                    redirectAttributes.addFlashAttribute("message", "Chỉ chấp nhận các file PNG, SVG, JPEG, AI, EPS.");
                    return "redirect:/company/update?id=" + idUser;
                }

                //  đường dẫn file
                Path filePath = uploadPath.resolve(uniqueFilename);
                // lưu tên file vào company
                Optional<Company> companyOptional = companyServiceImpl.findById(user.getCompany().getId());
                if (companyOptional.isPresent()) {
                    Company company = companyOptional.get();
                    company.setLogo(uniqueFilename);
                    companyServiceImpl.save(company);
                    // Copy file vào thư mục upload, nếu file đã tồn tại thì thay thế
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    redirectAttributes.addFlashAttribute("message",
                            "Bạn đã upload thành công file '" + uniqueFilename + "'!");
                } else {
                    redirectAttributes.addFlashAttribute("message", "Company không tồn tại.");
                    return "redirect:/company/update?id=" + idUser;
                }
            } else {
                redirectAttributes.addFlashAttribute("message", "User không tồn tại.");
                return "redirect:/company/update?id=" + idUser;
            }

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message",
                    "Upload thất bại: " + e.getMessage());
        }

        return "redirect:/company/update?id=" + idUser;
    }


}