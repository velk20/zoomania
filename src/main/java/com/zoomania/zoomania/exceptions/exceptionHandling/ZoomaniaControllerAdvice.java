package com.zoomania.zoomania.exceptions.exceptionHandling;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class ZoomaniaControllerAdvice {
    //Trimming every HTTP DTO attributes
    @InitBinder
    public void initBinder ( WebDataBinder binder )
    {
        StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmer);
    }
    @ExceptionHandler({Exception.class})
    public String handleError() {
        return "redirect:error";
    }
}
