package com.zoomania.zoomania.service;

import com.zoomania.zoomania.exceptions.OfferNotFoundException;
import com.zoomania.zoomania.exceptions.UserNotFoundException;
import com.zoomania.zoomania.model.dto.comment.CommentCreationDTO;
import com.zoomania.zoomania.model.entity.CommentEntity;
import com.zoomania.zoomania.model.entity.OfferEntity;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.model.view.CommentDisplayView;
import com.zoomania.zoomania.repository.CommentRepository;
import com.zoomania.zoomania.repository.OfferRepository;
import com.zoomania.zoomania.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, OfferRepository offerRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
    }

    public List<CommentDisplayView> getAllCommentsForOffer(Long offerId) {
        OfferEntity offer = offerRepository.findById(offerId).orElseThrow(() -> new OfferNotFoundException(offerId));

        List<CommentEntity> comments = commentRepository.findAllByOffer(offer).orElseThrow(RuntimeException::new);
        return comments.stream().map(comment -> new CommentDisplayView(comment.getId(), comment.getAuthor().getFullName(),
                comment.getText())).collect(Collectors.toList());
    }

    public CommentDisplayView createComment(CommentCreationDTO commentCreationDto) {
        UserEntity author = userRepository
                .findByUsername(commentCreationDto.getUsername())
                .orElseThrow(UserNotFoundException::new);

        OfferEntity offer = offerRepository.findById(commentCreationDto.getOfferId())
                .orElseThrow(() -> new OfferNotFoundException(commentCreationDto.getOfferId()));

        CommentEntity commentEntity = new CommentEntity()
                .setCreated(LocalDateTime.now())
                .setText(commentCreationDto.getMessage())
                .setAuthor(author)
                .setOffer(offer);

        commentRepository.save(commentEntity);

        return new CommentDisplayView(commentEntity.getId(), commentEntity.getAuthor().getFullName(), commentEntity.getText());
    }
}
