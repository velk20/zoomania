package com.zoomania.zoomania.web.rest;

import com.zoomania.zoomania.exceptions.CategoryNotFoundException;
import com.zoomania.zoomania.exceptions.OfferNotFoundException;
import com.zoomania.zoomania.exceptions.UserNotFoundException;
import com.zoomania.zoomania.model.view.OfferResponse;
import com.zoomania.zoomania.model.view.UserResponse;
import com.zoomania.zoomania.service.OfferService;
import com.zoomania.zoomania.service.UserService;
import com.zoomania.zoomania.util.RestPaginationConstants;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/offers/approve")
    public OfferResponse getAllNotApproveOffers(
            @RequestParam(value = "pageNo", defaultValue = RestPaginationConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = RestPaginationConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = RestPaginationConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = RestPaginationConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir

    ) {
        return offerService.getAllNotApproveOffers(pageNo, pageSize, sortBy, sortDir);
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    public String onUserNotFound(UserNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "error";
    }
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({CategoryNotFoundException.class})
    public String onCategoryNotFound(CategoryNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "error";
    }
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({OfferNotFoundException.class})
    public String onOfferNotFound(OfferNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "error";
    }
}
