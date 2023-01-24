package com.zoomania.zoomania.web.rest;

import com.zoomania.zoomania.model.view.OfferResponse;
import com.zoomania.zoomania.model.view.UserResponse;
import com.zoomania.zoomania.service.OfferService;
import com.zoomania.zoomania.service.UserService;
import com.zoomania.zoomania.util.RestPaginationConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam(value = "pageNo", defaultValue = RestPaginationConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = RestPaginationConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = RestPaginationConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = RestPaginationConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir

    ) {
        return userService.getAllUsersAdminRest(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/offers")
    public OfferResponse getAllOffers(
            @RequestParam(value = "pageNo", defaultValue = RestPaginationConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = RestPaginationConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = RestPaginationConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = RestPaginationConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir

    ) {
        return offerService.getAllOffersAdminRest(pageNo, pageSize, sortBy, sortDir);
    }
}
