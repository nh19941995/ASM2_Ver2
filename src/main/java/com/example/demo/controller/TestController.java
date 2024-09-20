package com.example.demo.controller;

import com.example.demo.entity.Recruitment;
import com.example.demo.service.RecruitmentService;
import com.example.demo.utility.AddAttributesToModel;
import com.example.demo.utility.PaginationRequest;
import com.example.demo.view.ViewConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/test")
public class TestController {
    private final RecruitmentService recruitmentService;
    private final AddAttributesToModel addAttributesToModel;

    @Autowired
    public TestController(RecruitmentService recruitmentService, AddAttributesToModel addAttributesToModel) {
        this.recruitmentService = recruitmentService;
        this.addAttributesToModel = addAttributesToModel;
    }


    @GetMapping("")
    public String test(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        PaginationRequest paginationRequest = new PaginationRequest(page, size, sortBy, sortDirection);
        Page<Recruitment> recruitmentPage = recruitmentService.findAll(paginationRequest);

        model.addAttribute("recruitmentPage", recruitmentPage);
        return ViewConstants.TEST;
    }


}