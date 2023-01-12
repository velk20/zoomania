package com.zoomania.zoomania.exceptionHandling;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ZoomaniaControllerAdvice {
    @ExceptionHandler({Exception.class})
    public String handleError() {
        return "redirect:error";
    }
}
