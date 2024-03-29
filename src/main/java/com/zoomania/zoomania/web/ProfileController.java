package com.zoomania.zoomania.web;

import com.zoomania.zoomania.exceptions.UserNotFoundException;
import com.zoomania.zoomania.model.dto.user.ChangeUserPasswordDTO;
import com.zoomania.zoomania.model.dto.user.UpdateUserDTO;
import com.zoomania.zoomania.model.view.UserDetailsView;
import com.zoomania.zoomania.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/users")
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile/{username}")
    public String profile(@PathVariable("username") String username, Model model) {

        if (!model.containsAttribute("user")) {
            UserDetailsView user = userService.getUser(username);
            model.addAttribute("user", user);
        }

        return "profile";
    }

    @PreAuthorize("@userService.isOwner(#principal.name,#username)")
    @PatchMapping("/profile/{username}/edit")
    public String updateProfile(@PathVariable("username") String username,
                                @Valid UpdateUserDTO editUser,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Principal principal) {
        if (bindingResult.hasErrors()) {
            editUser.setUsername(username);
            redirectAttributes.addFlashAttribute("user", editUser);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user",
                    bindingResult);

            return "redirect:/users/profile/{username}";
        }

        UserDetailsView userDetailsView = userService.editUser(username, editUser,principal);

        return "redirect:/users/profile/" + userDetailsView.getUsername();
    }

    @PreAuthorize("@userService.isOwner(#principal.name,#username)")
    @GetMapping("/profile/{username}/change_password")
    public String changePassword(@PathVariable("username") String username,
                                 Model model,
                                 Principal principal) {

        if (!model.containsAttribute("user")) {
            ChangeUserPasswordDTO user = new ChangeUserPasswordDTO();
            user.setUsername(username);
            model.addAttribute("user", user);
        }

        return "user-change-password";
    }

    @PreAuthorize("@userService.isOwner(#principal.name,#username)")
    @PatchMapping("/profile/{username}/change_password")
    public String changePassword(@PathVariable("username") String username,
                                @Valid ChangeUserPasswordDTO changeUserPasswordDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                 Principal principal) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", changeUserPasswordDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user",
                    bindingResult);

            return "redirect:/users/profile/{username}/change_password";
        }

        boolean changeUserPasswordSuccess = userService.changeUserPassword(changeUserPasswordDTO);
        if (!changeUserPasswordSuccess) {
            redirectAttributes.addFlashAttribute("bad_credentials", true);
            redirectAttributes.addFlashAttribute("user", changeUserPasswordDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changeUserPasswordDTO",
                    bindingResult);

            return "redirect:/users/profile/{username}/change_password";
        }

        return "redirect:/users/profile/" + username;
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    public String onUserNotFound(UserNotFoundException exception,Model model){
        model.addAttribute("message", exception.getMessage());
        return "error";
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({AccessDeniedException.class})
    public String onAccessDeniedException(AccessDeniedException exception,Model model){
        model.addAttribute("message", exception.getMessage());
        return "error";
    }

    @PreAuthorize("@userService.isOwner(#principal.name,#username)")
    @DeleteMapping("/profile/{username}/delete")
    public String deleteProfile(
            @PathVariable("username") String username,
            Principal principal,
            HttpServletRequest request
    ) throws ServletException {
        UserDetailsView userDetailsView = userService.deleteUserByUsername(username);
        if (userDetailsView.getUsername().equals(principal.getName())) {
            request.logout();
            return "index";
        }

        return "redirect:/admin/users";
    }
}
