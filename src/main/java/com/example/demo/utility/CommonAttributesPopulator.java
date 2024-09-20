package com.example.demo.utility;

import com.example.demo.service.CategoryService;
import com.example.demo.service.CompanyService;
import com.example.demo.service.RecruitmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Optional;

@Component
public class CommonAttributesPopulator {

    private final CategoryService categoryService;
    private final CompanyService companyService;
    private final AddAttributesToModel addAttributesToModel;
    private final RecruitmentService recruitmentService;

    @Autowired
    public CommonAttributesPopulator(CategoryService categoryService, CompanyService companyService, AddAttributesToModel addAttributesToModel, RecruitmentService recruitmentService) {
        this.categoryService = categoryService;
        this.companyService = companyService;
        this.addAttributesToModel = addAttributesToModel;
        this.recruitmentService = recruitmentService;
    }


    // tự động thêm các Attributes chung vào model
    public void userAuthenAndCompany(Model model) {

        // nếu đăng nhập thì thêm user vào model
        // nếu user có company thì thêm company vào model
        Optional.ofNullable(addAttributesToModel.addUserDtoByAuthen(model, "user"))
                .flatMap(user -> Optional.ofNullable(user.getCompany()))
                .ifPresent(company -> model.addAttribute("company", company));
    }



    public void addBestRecruiment(Model model) {
        model.addAttribute("bestPost", recruitmentService
                .bestPost(new PaginationRequest(0, 1, "id", "asc")));
    }
}
