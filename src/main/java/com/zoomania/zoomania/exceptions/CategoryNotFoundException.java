package com.zoomania.zoomania.exceptions;

public class CategoryNotFoundException extends RuntimeException{
    private final String message;

    public CategoryNotFoundException() {
        this.message = "Category was not found!";
    }

    public CategoryNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
