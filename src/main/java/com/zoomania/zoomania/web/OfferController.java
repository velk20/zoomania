package com.zoomania.zoomania.web;

import com.zoomania.zoomania.exceptions.OfferNotFoundException;
import com.zoomania.zoomania.model.dto.offer.CreateOrUpdateOfferDTO;
import com.zoomania.zoomania.model.dto.offer.SearchOfferDTO;
import com.zoomania.zoomania.model.view.OfferDetailsView;
import com.zoomania.zoomania.model.user.ZooManiaUserDetails;
import com.zoomania.zoomania.service.CategoryService;
import com.zoomania.zoomania.service.OfferService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/offers")
public class OfferController {
    private final OfferService offerService;
    private final CategoryService categoryService;

    public OfferController(OfferService offerService, CategoryService categoryService) {
        this.offerService = offerService;
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public String allOffers(
            Model model,
            @PageableDefault(
                    sort = "createdOn",
                    direction = Sort.Direction.DESC,
                    page = 0,
                    size = 8
            )Pageable pageable) {

        model.addAttribute("offers", offerService.getAllOffers(pageable));
        return "dashboard";
    }

    @GetMapping("/my")
    public String myOffers(
            Model model,
            @PageableDefault(
                    sort = "createdOn",
                    direction = Sort.Direction.DESC,
                    page = 0,
                    size = 8
            )Pageable pageable,
            @AuthenticationPrincipal ZooManiaUserDetails userDetails) {

        model.addAttribute("offers", offerService.getAllUserOffers(userDetails.getUsername(),pageable));
        return "user-offers";
    }

    @GetMapping("/create")
    public String createOffer(Model model) {
        if (!model.containsAttribute("addOfferModel")) {
            model.addAttribute("addOfferModel", new CreateOrUpdateOfferDTO());
        }
        model.addAttribute("categories", categoryService.getAllCategories());
        return "create-offer";
    }

    @PostMapping("/create")
    public String createOffers(@Valid CreateOrUpdateOfferDTO addOfferModel,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal ZooManiaUserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addOfferModel", addOfferModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addOfferModel",
                    bindingResult);
            return "redirect:/offers/create";
        }

        offerService.addOffer(addOfferModel, userDetails);

        return "redirect:/offers/all";
    }

    @GetMapping("/{id}/details")
    public String detailOffer(
            Model model,
            @PathVariable("id") Long id
    ) {
        OfferDetailsView offerById = offerService.getOfferById(id);
        model.addAttribute("offer", offerById);
        model.addAttribute("offerSellerUsername", offerById.getSellerUsername());
        return "details-offer";
    }

    @PreAuthorize("@offerService.isOwner(#principal.username,#id)")
    @GetMapping("/{id}/edit")
    public String editOffer(
            Model model,
            @PathVariable("id") Long id
    ) {

        if (!model.containsAttribute("editOffer")) {
            CreateOrUpdateOfferDTO offerById = offerService.getEditOfferById(id);
            model.addAttribute("editOffer", offerById);
        }
        model.addAttribute("offerId", id);

        return "edit-offer";
    }

    @PatchMapping("/{id}/edit")
    public String editOffer(
                            @PathVariable("id") Long id,
                            @Valid CreateOrUpdateOfferDTO editOffer,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("editOffer", editOffer);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editOffer",
                    bindingResult);
            return "redirect:/offers/{id}/edit";
        }

        offerService.editOffer(id,editOffer);

        return "redirect:/offers/{id}/details";
    }

    @GetMapping("/search")
    public String searchQuery(@Valid SearchOfferDTO searchOfferDTO,
                              BindingResult bindingResult,
                              Model model,
                              @PageableDefault(
                                      sort = "createdOn",
                                      direction = Sort.Direction.DESC,
                                      page = 0,
                                      size = 8
                              )Pageable pageable) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("searchOfferModel", searchOfferDTO);
            model.addAttribute(
                    "org.springframework.validation.BindingResult.searchOfferModel",
                    bindingResult);
            return "search-offer";
        }

        if (!model.containsAttribute("searchOfferModel")) {
            model.addAttribute("searchOfferModel", searchOfferDTO);
        }

        if (!searchOfferDTO.isEmpty()) {
            model.addAttribute("offers", offerService.searchOffer(searchOfferDTO,pageable));
        }

        return "search-offer";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteOffer(
            @PathVariable("id") Long id
    ) {
        boolean deleteOfferById = offerService.deleteOfferById(id);

        return "redirect:/dashboard";
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({OfferNotFoundException.class})
    public String onProductNotFound(OfferNotFoundException onfe,Model model){
        model.addAttribute("message", onfe.getMessage());
        return "error";
    }
}
