package com.zoomania.zoomania.service;

import com.zoomania.zoomania.exceptions.CommentNotFoundException;
import com.zoomania.zoomania.exceptions.OfferNotFoundException;
import com.zoomania.zoomania.exceptions.UserNotFoundException;
import com.zoomania.zoomania.model.dto.comment.CommentCreationDTO;
import com.zoomania.zoomania.model.entity.CommentEntity;
import com.zoomania.zoomania.model.entity.OfferEntity;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.model.view.CommentDisplayView;
import com.zoomania.zoomania.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final OfferService offerService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, OfferService offerService, UserService userService) {
        this.commentRepository = commentRepository;
        this.offerService = offerService;
        this.userService = userService;
    }


    public Optional<List<CommentEntity>> findAllByOffer(OfferEntity offer) {
        return this.commentRepository.findAllByOffer(offer);
    }

    public List<CommentDisplayView> getAllCommentsForOffer(Long offerId) {
        OfferEntity offer = offerService
                .findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException(offerId));

        List<CommentEntity> comments =
                this.findAllByOffer(offer)
                .orElseThrow(CommentNotFoundException::new);

        return comments.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public CommentDisplayView map(CommentEntity comment) {
        return new CommentDisplayView(
                comment.getId(),
                comment.getAuthor().getFullName(),
                comment.getText()
        );
    }
    public CommentDisplayView createComment(CommentCreationDTO commentCreationDto) {
        UserEntity author = userService
                .findByUsername(commentCreationDto.getUsername())
                .orElseThrow(UserNotFoundException::new);

        OfferEntity offer = offerService
                .findById(commentCreationDto.getOfferId())
                .orElseThrow(() -> new OfferNotFoundException(commentCreationDto.getOfferId()));

        CommentEntity commentEntity = new CommentEntity()
                .setCreated(LocalDateTime.now())
                .setText(commentCreationDto.getMessage())
                .setAuthor(author)
                .setOffer(offer);

        commentRepository.save(commentEntity);

        return this.map(commentEntity);
    }
}
