package com.zoomania.zoomania.service;

import com.zoomania.zoomania.exceptions.CommentNotFoundException;
import com.zoomania.zoomania.exceptions.OfferNotFoundException;
import com.zoomania.zoomania.model.dto.comment.CommentCreationDTO;
import com.zoomania.zoomania.model.entity.CommentEntity;
import com.zoomania.zoomania.model.entity.OfferEntity;
import com.zoomania.zoomania.model.entity.UserEntity;
import com.zoomania.zoomania.model.view.CommentDisplayView;
import com.zoomania.zoomania.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private OfferService offerService;
    @Mock
    private UserService userService;

    private CommentService commentService;
    private CommentEntity commentEntity;
    private OfferEntity offerEntity;
    @BeforeEach
    public void setUp() {
        this.offerEntity = new OfferEntity()
                .setTitle("offer title");

        offerEntity.setId(1L);

        this.commentService = new CommentService(commentRepository, offerService, userService);
        this.commentEntity = new CommentEntity(1L, LocalDateTime.now(), "TEXT", new UserEntity(), new OfferEntity());
    }

    @Test
    public void findAllByOfferTest() {

        when(commentRepository.findAllByOffer(offerEntity))
                .thenReturn(Optional.of(List.of(commentEntity)));

        List<CommentEntity> allByOffer = commentService.findAllByOffer(offerEntity).get();

        assertEquals(allByOffer.size(), 1);
        CommentEntity comment = allByOffer.get(0);

        assertEquals(comment.getId(), commentEntity.getId());
    }

    @Test
    public void getAllCommentsForOfferTest() {
        when(offerService.findById(offerEntity.getId()))
                .thenReturn(Optional.of(offerEntity));

        when(commentService.findAllByOffer(offerEntity))
                .thenReturn(Optional.of(List.of(commentEntity)));

        List<CommentEntity> commentEntities = commentService.findAllByOffer(offerEntity).get();

        assertEquals(commentEntities.size(), 1);
        List<CommentDisplayView> allCommentsForOffer = commentService.getAllCommentsForOffer(offerEntity.getId());

        assertEquals(allCommentsForOffer.size(), 1);
        assertTrue(allCommentsForOffer
                .stream()
                .anyMatch(c -> c.getId().equals(commentEntity.getId())));
    }

    @Test
    public void getAllCommentsForOfferTest_ThrowsOfferNotFound() {
        assertThatThrownBy(() ->
                commentService.getAllCommentsForOffer(offerEntity.getId())
        ).isInstanceOf(OfferNotFoundException.class)
                .hasMessage("Offer with id " + offerEntity.getId() + " was not found!");

    }

    @Test
    public void getAllCommentsForOfferTest_ThrowsCommentNotFound() {
        when(offerService.findById(offerEntity.getId()))
                .thenReturn(Optional.of(offerEntity));

        assertThatThrownBy(() ->
                commentService.getAllCommentsForOffer(offerEntity.getId())
        ).isInstanceOf(CommentNotFoundException.class)
                .hasMessage("Comment was not found!");
    }

    @Test
    public void mapCommentTest() {
        CommentDisplayView map = commentService.map(commentEntity);
        assertEquals(map.getId(), commentEntity.getId());
        assertEquals(map.getMessage(), commentEntity.getText());
    }

    @Test
    public void createCommentTest() {
        CommentCreationDTO commentCreationDTO =
                new CommentCreationDTO("admin", offerEntity.getId(), "message");

        UserEntity user = new UserEntity()
                .setUsername("admin");
        user.setId(1L);

        when(userService.findByUsername(commentCreationDTO.getUsername()))
                .thenReturn(Optional.of(user));
        when(offerService.findById(commentCreationDTO.getOfferId()))
                .thenReturn(Optional.of(offerEntity));

        CommentDisplayView comment = this.commentService.createComment(commentCreationDTO);

        assertEquals(comment.getMessage(), commentCreationDTO.getMessage());
    }
}
