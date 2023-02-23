package com.zoomania.zoomania.web;

import com.zoomania.zoomania.exceptions.UserNotFoundException;
import com.zoomania.zoomania.model.dto.user.UserRegisterDTO;
import com.zoomania.zoomania.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")

public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("userModel")
    public UserRegisterDTO initUserModel() {
        return new UserRegisterDTO();
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            RedirectAttributes redirectAttributes
    ) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        if (this.userService.isUserActive(username)) {
            redirectAttributes.addFlashAttribute("inactive_user", true);

        }else{
            redirectAttributes.addFlashAttribute("bad_credentials", true);
        }

        return "redirect:/users/login";
    }
    @PostMapping("/register")
    public String register( @Valid UserRegisterDTO userModel,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userModel", userModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userModel",
                    bindingResult);
            return "redirect:/users/register";
        }
        userService.registerAndLogin(userModel);

        return "redirect:/";
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    public String onUserNotFound(UserNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "error";
    }

}
