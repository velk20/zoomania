package com.zoomania.zoomania.exceptions;

public class CommentNotFoundException extends RuntimeException {
    private final String message;

    public CommentNotFoundException() {
        this.message = "Comment was not found!";
    }

    public CommentNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
