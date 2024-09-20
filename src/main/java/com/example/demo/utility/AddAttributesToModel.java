package com.example.demo.utility;

import com.example.demo.controller.dto.UserUpdate;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.*;
import com.example.demo.service.crud.FindById;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;


@Service
public class AddAttributesToModel {

    private final UserService userService;
    private final TypeService typeService;
    private final CategoryService categoryService;
    private final RecruitmentService recruitmentService;

    public AddAttributesToModel(UserService userService, TypeService typeService, CategoryService categoryService, RecruitmentService recruitmentService) {
        this.userService = userService;
        this.typeService = typeService;
        this.categoryService = categoryService;
        this.recruitmentService = recruitmentService;
    }

    public <T, S extends FindById<T, Long>> void addAttribute(
            Model model,
            Long userId,
            //  Function<User, T> để truyền phương thức getter của entity từ User (ví dụ: User::getCompany)
            Function<User, T> entityGetter,
            // BaseService<T, Long> để truyền service của entity (ví dụ: CompanyService)
            S entityService,
            String attributeName,
            // Supplier<T> để cung cấp một cách tạo entity mới mặc định (ví dụ: Company::new).
            Supplier<T> defaultEntitySupplier,
            Class<T> entityClass
    ) {
        // kiểm tra xem user có tồn tại không
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // lấy entity từ user
        T entity = Optional.ofNullable(
                        // truyền phương thức getter của entity từ User
                        entityGetter.apply(user)
                )
                .flatMap(e -> Optional.ofNullable(getEntityId(e))
                        .flatMap(entityService::findById))
                .orElseGet(defaultEntitySupplier);

        model.addAttribute(attributeName, entity);
    }

    // để lấy ID của entity một cách động, sử dụng reflection.
    // giúp tránh phải cast entity về một type cụ thể
    private <T> Long getEntityId(T entity) {
        try {
            Method getIdMethod = entity.getClass().getMethod("getId");
            return (Long) getIdMethod.invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }

    public void addCompany(Model model, Long userId, CompanyService companyService, String attributeName) {
        addAttribute(model, userId, User::getCompany, companyService, attributeName, Company::new, Company.class);
    }

    public void addRole(Model model, Long userId, RoleService roleService) {
        addAttribute(model, userId, User::getRole, roleService, "role", Role::new, Role.class);
    }

    public void addType(Model model, String attributeName) {
        model.addAttribute(attributeName, typeService.findAll());
    }

    public void addCategory(Model model, String attributeName) {
        model.addAttribute(attributeName, categoryService.findAll());
    }

    public void addCv(Model model, Long userId, CvService cvService) {
        addAttribute(model, userId, User::getCv, cvService, "cv", Cv::new, Cv.class);
    }

    public void addUserDtoById(Model model, Long userId, String attributeName) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserUpdate userDto = new UserUpdate(user);
        model.addAttribute(attributeName, userDto);
    }

    public User addUserDtoByAuthen(Model model, String attributeName) {
        return getAuthenticatedUsername()
                .flatMap(userService::findByUsername)
                .map(user -> {
                    model.addAttribute(attributeName, new UserUpdate(user));
                    return user;
                })
                .orElse(null);
    }

    private Optional<String> getAuthenticatedUsername() {
        return Optional.ofNullable(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName)
                .filter(name -> !name.equals("anonymousUser"));
    }


    public void addAvatar(Model model, Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String avatar = user.getImage();
        if (avatar != null) {
            model.addAttribute("avatar", avatar);
        } else {
            model.addAttribute("avatar", "default-avatar.png"); // hoặc không thêm gì cả
        }
    }

    public void addRecruitment(Model model, Long recruitmentId, String attributeName) {
        Recruitment recruitment = recruitmentService.findById(recruitmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruitment not found"));
        model.addAttribute(attributeName, recruitment);
    }
}