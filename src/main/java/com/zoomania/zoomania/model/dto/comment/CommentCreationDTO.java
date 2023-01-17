package com.zoomania.zoomania.model.dto.comment;

public class CommentCreationDTO {
    private String username;
    private Long offerId;
    private String message;

    public CommentCreationDTO(String username, Long offerId, String message) {
        this.username = username;
        this.offerId = offerId;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
