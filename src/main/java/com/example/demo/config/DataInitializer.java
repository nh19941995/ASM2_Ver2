package com.example.demo.config;

import com.example.demo.entity.*;
import com.example.demo.service.*;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleService roleService;
    private final StatusService statusService;
    private final UserService userService;
    private final TypeService typeService;
    private final CategoryService categoryService;
    private final CompanyService companyService;

    @Autowired
    public DataInitializer(
            RoleService roleService, StatusService statusService,
            UserService userService, TypeService typeService,
            CategoryService categoryService,
            CompanyService companyService
    ) {
        this.roleService = roleService;
        this.statusService = statusService;
        this.userService = userService;
        this.typeService = typeService;
        this.categoryService = categoryService;
        this.companyService = companyService;
    }

    @PostConstruct
    @Transactional
    public void init() {
        if (roleService.count() == 0) {
            Role USER = new Role(EnumRole.USER.getName(), EnumRole.USER.getClassCss());
            Role MANAGER = new Role(EnumRole.MANAGER.getName(), EnumRole.MANAGER.getClassCss());

            roleService.save(USER);
            roleService.save(MANAGER);

            logger.info("Roles initialized successfully");
        }

        if (statusService.count() == 0) {
            Status ACTIVE = new Status(EnumStatus.ACTIVE.getName(), "User is active", EnumStatus.ACTIVE.getClassCss());
            Status BLOCK = new Status(EnumStatus.BLOCK.getName(), "User is block", EnumStatus.BLOCK.getClassCss());
            Status INACTIVE = new Status(EnumStatus.INACTIVE.getName(), "User is inactive", EnumStatus.INACTIVE.getClassCss());

            statusService.save(ACTIVE);
            statusService.save(BLOCK);
            statusService.save(INACTIVE);

            logger.info("Statuses initialized successfully");
        }

        if (typeService.count() == 0) {
            Type type1 = new Type("Full Time", "Full Time");
            Type type2 = new Type("Part Time", "Part Time");

            typeService.save(type1);
            typeService.save(type2);
            logger.info("Types initialized successfully");
        }

        if (categoryService.count() == 0) {
            Category category1 = new Category("Java", 0);
            Category category2 = new Category("Python", 0);
            Category category3 = new Category("C#", 0);
            Category category4 = new Category("C++", 0);
            Category category5 = new Category("Ruby", 0);
            Category category6 = new Category("PHP", 0);
            Category category7 = new Category("JavaScript", 0);
            Category category8 = new Category("HTML", 0);

            categoryService.save(category1);
            categoryService.save(category2);
            categoryService.save(category3);
            categoryService.save(category4);
            categoryService.save(category5);
            categoryService.save(category6);
            categoryService.save(category7);
            categoryService.save(category8);

            logger.info("Categories initialized successfully");
        }

        if (userService.count() == 0) {
            userService.addDataUser( "Nguyễn Văn An",
                    "nguyenvanan1", "nguyenvanan1",
                    "nguyenvanan1@gmail.com", "Ha Noi",
                    "0123456801", "Software Developer",
                    1L, 1L
            );

            userService.addDataUser( "Trần Thị Bình", "tranthivinh2",
                    "tranthivinh2", "tranthivinh2@gmail.com",
                    "Ho Chi Minh City", "0123456802",
                    "Data Analyst", 2L, 2L
            );

            userService.addDataUser(
                    "Lê Văn Cường", "levancuong3",
                    "levancuong3", "levancuong3@gmail.com",
                    "Da Nang", "0123456803",
                    "Project Manager", 1L, 1L
            );

            userService.addDataUser(
                    "Phạm Thị Dung", "phamthidung4",
                    "phamthidung4", "phamthidung4@gmail.com",
                    "Hai Phong", "0123456804",
                    "UI/UX Designer", 2L, 2L
            );

            userService.addDataUser(
                    "Hoàng Văn Quyên", "hoangvanem5",
                    "hoangvanem5", "hoangvanem5@gmail.com",
                    "Can Tho", "0123456805",
                    "System Administrator", 1L, 1L
            );

            userService.addDataUser(
                    "Nguyễn Thị Phùng", "nguyenthifung6",
                    "nguyenthifung6", "nguyenthifung6@gmail.com",
                    "Nha Trang", "0123456806",
                    "Marketing Specialist", 2L, 2L
            );

            userService.addDataUser(
                    "Trần Văn Giang", "tranvangiang7",
                    "tranvangiang7", "tranvangiang7@gmail.com",
                    "Vung Tau", "0123456807",
                    "Sales Representative", 1L, 1L
            );

            userService.addDataUser(
                    "Le Thi Huong", "lethihuong8",
                    "lethihuong8", "lethihuong8@gmail.com",
                    "Hue", "0123456808",
                    "HR Manager", 2L, 2L
            );

            userService.addDataUser(
                    "Pham Van Inh", "phamvaninh9",
                    "phamvaninh9", "phamvaninh9@gmail.com",
                    "Quy Nhon", "0123456809",
                    "Financial Analyst", 1L, 1L
            );

            userService.addDataUser(
                    "Hoàng Thị Kim", "hoangthikim10",
                    "hoangthikim10", "hoangthikim10@gmail.com",
                    "Bien Hoa", "0123456810",
                    "Content Writer", 2L, 2L
            );

            userService.addDataUser(
                    "Nguyễn Văn Lâm", "nguyenvanlam11",
                    "nguyenvanlam11", "nguyenvanlam11@gmail.com",
                    "Thai Nguyen", "0123456811",
                    "Network Engineer", 1L, 1L
            );



        }



    }
}