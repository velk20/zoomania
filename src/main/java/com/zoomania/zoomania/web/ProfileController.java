package com.zoomania.zoomania.web;

import com.zoomania.zoomania.model.view.UserDetailsView;
import com.zoomania.zoomania.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile/{username}")
    public String profile(@PathVariable("username") String username, Model model) {
        UserDetailsView user = userService.getUser(username);
        model.addAttribute("user", user);
        return "profile";
    }
}
