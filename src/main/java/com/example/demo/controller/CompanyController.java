package com.example.demo.controller;

import com.example.demo.controller.dto.UserUpdate;
import com.example.demo.entity.*;
import com.example.demo.service.*;
import com.example.demo.utility.AddAttributesToModel;
import com.example.demo.utility.CommonAttributesPopulator;
import com.example.demo.utility.FileStorageService;
import com.example.demo.view.ViewConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/company")
public class CompanyController {

    private final UserService userService;
    private final CvService cvService;
    private final RoleService roleService;
    private final CompanyService companyService;
    private final AddAttributesToModel addAttributesToModel;
    private final FileStorageService fileStorageService;
    private final CommonAttributesPopulator commonAttributesPopulator;

    @Autowired
    public CompanyController(UserService userService, CvService cvService, RoleService roleService, CompanyService companyService, AddAttributesToModel addAttributesToModel, FileStorageService fileStorageService,  CommonAttributesPopulator commonAttributesPopulator) {
        this.userService = userService;
        this.cvService = cvService;
        this.roleService = roleService;
        this.companyService = companyService;
        this.addAttributesToModel = addAttributesToModel;
        this.fileStorageService = fileStorageService;
        this.commonAttributesPopulator = commonAttributesPopulator;
    }

    // Phương thức được đánh dấu bằng @ModelAttribute sẽ được gọi
    // trước khi bất kỳ phương thức xử lý request nào
    // trong controller được thực thi.
    @ModelAttribute
    public void addCommonAttributes(Model model) {
        commonAttributesPopulator.userAuthenAndCompany(model);
        commonAttributesPopulator.addBestRecruiment(model);
    }

    // update thông tin công ty
    // http://localhost:8080/company/update?id=1
    @PostMapping("/updateInormation")
    public String updateUser(@Valid @ModelAttribute("company") Company company,
                             BindingResult bindingResult,
                             @RequestParam("file") MultipartFile file,
                             Model model,
                             RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addAttributesToModel.addUserDtoById(model, company.getUser().getId(),"user");
            addAttributesToModel.addCv(model, company.getUser().getId(), cvService);
            addAttributesToModel.addRole(model, company.getUser().getId(), roleService);
            return ViewConstants.PROFILE_VIEW;
        }

        // Lưu file logo
        if (!file.isEmpty()) {
            fileStorageService.saveLogo(file, company, redirectAttributes);
        }else {
            company.setLogo(companyService.findById(company.getId()).get().getLogo());
        }

        companyService.save(company);
        redirectAttributes.addFlashAttribute("messages", "Cập nhật thông tin công ty thành công !");
        return "redirect:/user/update?id=" + company.getUser().getId();
    }

    // update thông tin quản lý của công ty
    // http://localhost:8080/company/updateManager?id=1
    @PostMapping("/updateManager")
    public String updateUser(@Valid @ModelAttribute("user") UserUpdate userDto,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            addAttributesToModel.addCv(model, userDto.getId(), cvService);
            addAttributesToModel.addCompany(model, userDto.getId(), companyService, "company");
            addAttributesToModel.addRole(model, userDto.getId(), roleService);
            return ViewConstants.PROFILE_VIEW;
        }

        // lưu user trước khi lưu file CV
        User user = new User(userDto);
        userService.save(user);  // Lưu user và lấy user đã được lưu

        redirectAttributes.addFlashAttribute("messages", "Cập nhật thông tin thành công");
        return "redirect:/user/update?id=" + userDto.getId();
    }

    // xem chi tiết công ty
    // http://localhost:8080/company/detail?companyId=1
    @GetMapping("/detail")
    public String detailCompany(
            @RequestParam("companyId") Long companyId,
            Model model
    ) {
        Company company = companyService.findById(companyId).
                orElseThrow(() -> new RuntimeException("Company not found"));
        model.addAttribute("company", company);
        return ViewConstants.POST_COMPANY;
    }


}
