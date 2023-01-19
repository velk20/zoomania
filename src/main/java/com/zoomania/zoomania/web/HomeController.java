package com.zoomania.zoomania.web;

import com.zoomania.zoomania.service.CloudinaryService;
import com.zoomania.zoomania.service.OfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class HomeController {
    private final static int HOME_RECENT_OFFERS_COUNT = 3;

    private final OfferService offerService;

    public HomeController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("recentOffers",
                offerService.getRecentOffers(HOME_RECENT_OFFERS_COUNT));
        return "index";
    }

}
