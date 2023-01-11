package com.zoomania.zoomania.web.rest;

import com.zoomania.zoomania.model.view.UserDetailsView;
import com.zoomania.zoomania.service.OfferService;
import com.zoomania.zoomania.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminRestController {
    private final OfferService offerService;
    private final UserService userService;

    public AdminRestController(OfferService offerService, UserService userService) {
        this.offerService = offerService;
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDetailsView>> getAllUsers() {
        List<UserDetailsView> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }
}
