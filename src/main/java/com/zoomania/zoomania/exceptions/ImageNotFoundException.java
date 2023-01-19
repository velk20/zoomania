package com.zoomania.zoomania.exceptions;

public class ImageNotFoundException extends RuntimeException{
    private final String message;

    public ImageNotFoundException() {
        this.message = "Image was not found!";
    }

    public ImageNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
