package com.example.demo.utility;

import com.example.demo.entity.Company;
import com.example.demo.entity.Cv;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.CvServiceImpl;
import com.example.demo.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class FileStorageService {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path cvFileStorageLocation;
    private final Path logoFileStorageLocation;
    private final Path avatarFileStorageLocation;

    private final CvServiceImpl cvServiceImpl;
    private final UserServiceImpl userServiceImpl;

    public FileStorageService(
            @Value("${file.upload-dir-cv}") String uploadDirCv,
            @Value("${file.upload-dir-logo}") String uploadDirLogo,
            @Value("${file.upload-dir-avatar}") String uploadDirAvatar,
            CvServiceImpl cvServiceImpl,
            UserServiceImpl userServiceImpl
    ) {
        this.userServiceImpl = userServiceImpl;
        this.cvServiceImpl = cvServiceImpl;

        this.cvFileStorageLocation = Path.of(uploadDirCv).toAbsolutePath().normalize();
        this.logoFileStorageLocation = Path.of(uploadDirLogo).toAbsolutePath().normalize();
        this.avatarFileStorageLocation = Path.of(uploadDirAvatar).toAbsolutePath().normalize();
        createDirectories();
    }

    // tạo thư mục lưu trữ file
    private void createDirectories() {
        try {
            Files.createDirectories(this.cvFileStorageLocation);
            Files.createDirectories(this.logoFileStorageLocation);
            Files.createDirectories(this.avatarFileStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Không thể tạo thư mục lưu trữ file.", ex);
        }
    }

    @Transactional
    public boolean saveCv(
            MultipartFile file,
            User user,
            RedirectAttributes redirectAttributes
    ) {
        // file trống thì bỏ qua
        if (isInvalidFile(file, redirectAttributes)) {
            return true;
        }

        // file.getOriginalFilename(): Lấy tên gốc của file được tải lên
        // Objects.requireNonNull(...): Đảm bảo rằng tên file không phải là null. Nếu là null, sẽ ném ra ngoại lệ NullPointerException.
        // StringUtils.cleanPath(...): Làm sạch tên file bằng cách loại bỏ các ký tự không hợp lệ hoặc không cần thiết trong đường dẫn.
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // StringUtils.getFilenameExtension(...): Lấy phần mở rộng của file từ tên file gốc đã được làm sạch.
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);

        // Kiểm tra phần mở rộng của file tải lên.
        //Nếu phần mở rộng không phải là PDF, thêm thông báo lỗi vào redirectAttributes và kết thúc phương thức.
        if (!AcceptedFileType.isCvAccepted(fileExtension)) {
            redirectAttributes.addFlashAttribute("messages", "Chỉ chấp nhận file PDF.");
            return false;
        }

        // Tạo tên file duy nhất theo username của user.
        // Sử dụng tên file gốc đã được làm sạch (originalFilename).
        String uniqueFilename = generateUniqueFilename(user, originalFilename);

        // Lưu tên file vào CV của user và lưu CV.
        Cv cv = getOrCreateCv(user);

        // Lưu tên file vào CV của user và lưu CV.
        cv.setFileName(uniqueFilename);

        cvServiceImpl.save(cv);
        userServiceImpl.save(user);

        saveFile(file, uniqueFilename, cvFileStorageLocation);
        return true;
    }

    public void saveLogo(
            MultipartFile file,
            Company company,
            RedirectAttributes redirectAttributes
    ){
        // thông báo lỗi nếu file trống hoặc không hợp lệ
        if (isInvalidFile(file, redirectAttributes)) {
            return;
        }

        // Lấy tên gốc của file được tải lên và phần mở rộng của file
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);

        // Kiểm tra phần mở rộng của file tải lên.
        if (!AcceptedFileType.isLogoAccepted(fileExtension)) {
            redirectAttributes.addFlashAttribute("messages", "File không đúng định dạng");
            return;
        }

        User user = userServiceImpl.findById(company.getUser().getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String uniqueFilename = generateUniqueFilename(user, originalFilename);

        company.setLogo(uniqueFilename);

        saveFile(file, uniqueFilename, logoFileStorageLocation);
    }

    public boolean saveAvatar(
            MultipartFile file,
            User user,
            RedirectAttributes redirectAttributes
    ){
        // thông báo lỗi nếu file trống hoặc không hợp lệ
        if (isInvalidFile(file, redirectAttributes)) {
            return true;
        }

        // Lấy tên gốc của file được tải lên và phần mở rộng của file
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);

        // Kiểm tra phần mở rộng của file tải lên.
        if (!AcceptedFileType.isImageAccepted(fileExtension)) {
            redirectAttributes.addFlashAttribute("messages", "File không đúng định dạng");
            return false;
        }

        String uniqueFilename = generateUniqueFilename(user, originalFilename);

        user.setImage(uniqueFilename);
        userServiceImpl.save(user);
        saveFile(file, uniqueFilename, avatarFileStorageLocation);
        return true;
    }




    // thông báo lỗi nếu file trống hoặc không hợp lệ
    private boolean isInvalidFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file == null || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Không có file được chọn hoặc file trống.");
            return true;
        }
        return false;
    }

    // tạo tên file duy nhất theo username
    private String generateUniqueFilename(User user, String originalFilename) {
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);
        return user.getUsername() + "." + fileExtension;
    }

    // lấy hoặc tạo mới CV cho user nếu user chưa có CV
    private Cv getOrCreateCv(User user) {
        return Optional.ofNullable(user.getCv()).orElseGet(() -> {
            Cv newCv = new Cv();
            user.setCv(newCv);
            return newCv;
        });
    }

    // hàm lưu file
    private void saveFile(MultipartFile file, String uniqueFilename, Path storageLocation) {
        Path targetLocation = storageLocation.resolve(uniqueFilename);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File {} saved successfully", uniqueFilename);
        } catch (IOException ex) {
            throw new FileStorageException("Lỗi khi lưu file " + uniqueFilename, ex);
        }
    }


    private void deleteFile(
            String fileName,
            String type
    ) {
        try {
            Path targetLocation = switch (type) {
                case "cv" -> this.cvFileStorageLocation.resolve(fileName);
                case "logo" -> this.logoFileStorageLocation.resolve(fileName);
                case "avatar" -> this.avatarFileStorageLocation.resolve(fileName);
                default -> throw new IllegalArgumentException("Invalid file type");
            };
            Files.deleteIfExists(targetLocation);
            logger.info("File {} deleted successfully", fileName);
        } catch (IOException ex) {
            throw new FileStorageException("Lỗi khi xóa file " + fileName, ex);
        }
    }

    public void deleteCvFile(String fileName) {
        deleteFile(fileName, "cv");
    }

    public void deleteLogoFile(String fileName) {
        deleteFile(fileName, "logo");
    }

    public void deleteAvatarFile(String fileName) {
        deleteFile(fileName, "avatar");
    }

    enum AcceptedFileType {
        PDF("pdf", FileCategory.CV),
        DOC("doc", FileCategory.CV),
        DOCX("docx", FileCategory.CV),
        PNG("png", FileCategory.LOGO, FileCategory.IMAGE),
        JPG("jpg", FileCategory.LOGO, FileCategory.IMAGE),
        JPEG("jpeg", FileCategory.LOGO, FileCategory.IMAGE),
        SVG("svg", FileCategory.LOGO, FileCategory.IMAGE);

        private final String extension;
        private final Set<FileCategory> categories;

        // Dấu ba chấm ... sau kiểu dữ liệu FileCategory cho phép phương
        // thức nhận một số lượng đối số kiểu FileCategory không cố định.

        // Điều này có nghĩa là bạn có thể truyền vào một, hai,
        // hoặc nhiều FileCategory khi khởi tạo một AcceptedFileType.

        // Bên trong phương thức, categories được xử lý như một mảng của FileCategory.
        AcceptedFileType(String extension, FileCategory... categories) {
            this.extension = extension;
            // EnumSet là một set chứa các enum
            this.categories = EnumSet.copyOf(Arrays.asList(categories));
        }

        public static boolean isFileAccepted(String fileExtension, FileCategory category) {
            return Arrays.stream(values())
                    .filter(type -> type.categories.contains(category))
                    .anyMatch(type -> type.extension.equalsIgnoreCase(fileExtension));
        }

        public static boolean isCvAccepted(String fileExtension) {
            return isFileAccepted(fileExtension, FileCategory.CV);
        }

        public static boolean isLogoAccepted(String fileExtension) {
            return isFileAccepted(fileExtension, FileCategory.LOGO);
        }

        public static boolean isImageAccepted(String fileExtension) {
            return isFileAccepted(fileExtension, FileCategory.IMAGE);
        }

        enum FileCategory {
            CV, LOGO, IMAGE
        }
    }




    // thông báo lỗi khi lưu file
    public static class FileStorageException extends RuntimeException {
        public FileStorageException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}