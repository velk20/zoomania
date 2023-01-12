package com.zoomania.zoomania.web.rest;

import com.zoomania.zoomania.model.view.UserDetailsView;
import com.zoomania.zoomania.model.view.UserResponse;
import com.zoomania.zoomania.service.OfferService;
import com.zoomania.zoomania.service.UserService;
import com.zoomania.zoomania.util.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public UserResponse getAllUsers(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir

    ) {
        return userService.getAllUsersAdminRest(pageNo, pageSize, sortBy, sortDir);
    }
}
