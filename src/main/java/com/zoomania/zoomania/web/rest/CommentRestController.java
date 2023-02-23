package com.zoomania.zoomania.web.rest;

import com.zoomania.zoomania.exceptions.CommentNotFoundException;
import com.zoomania.zoomania.exceptions.OfferNotFoundException;
import com.zoomania.zoomania.exceptions.UserNotFoundException;
import com.zoomania.zoomania.model.dto.comment.CommentCreationDTO;
import com.zoomania.zoomania.model.dto.comment.CommentMessageDTO;
import com.zoomania.zoomania.model.user.ZooManiaUserDetails;
import com.zoomania.zoomania.model.view.CommentDisplayView;
import com.zoomania.zoomania.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentRestController {
    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{offerId}/comments")
    public ResponseEntity<List<CommentDisplayView>> getComments(@PathVariable("offerId") Long offerId) {
        return ResponseEntity.ok(commentService.getAllCommentsForOffer(offerId));
    }

    @PostMapping(value = "/{offerId}/comments", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CommentDisplayView> createComment(@PathVariable("offerId") Long offerId,
                                                            @AuthenticationPrincipal ZooManiaUserDetails userDetails,
                                                            @RequestBody CommentMessageDTO commentDto) {
        CommentCreationDTO commentCreationDto =
                new CommentCreationDTO(
                userDetails.getUsername(),
                offerId,
                commentDto.getMessage()
        );

        CommentDisplayView comment = commentService.createComment(commentCreationDto);

        return ResponseEntity
                .created(URI.create(String.format("/api/%d/comments/%d", offerId, comment.getId())))
                .body(comment);
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class})
    public String onUserNotFound(UserNotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "error";
    }
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({CommentNotFoundException.class})
    public String onCommentNotFound(CommentNotFoundException exception, Model model){
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
