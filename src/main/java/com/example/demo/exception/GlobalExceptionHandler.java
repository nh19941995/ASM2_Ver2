package com.example.demo.exception;

import com.example.demo.view.ViewConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorDetails", errorDetails);
        modelAndView.setViewName(ViewConstants.EXCEPTION_VIEW);
        return modelAndView;
    }

    @ExceptionHandler(BadRequestException.class)
    public ModelAndView handleBadRequestException(BadRequestException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorDetails", errorDetails);
        modelAndView.setViewName(ViewConstants.EXCEPTION_VIEW);
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGlobalException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorDetails", errorDetails);
        modelAndView.setViewName(ViewConstants.EXCEPTION_VIEW);
        return modelAndView;
    }


}