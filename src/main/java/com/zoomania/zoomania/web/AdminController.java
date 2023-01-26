package com.zoomania.zoomania.web;

import com.zoomania.zoomania.web.rest.AdminRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/users")
    public String adminUsers() {
        return "admin-users";
    }

    @GetMapping("/offers")
    public String adminOffers() {
        return "admin-offers";
    }

    @GetMapping("/offers/approve")
    public String adminApproveOffers() {
        return "admin-approve-offers";
    }
}
