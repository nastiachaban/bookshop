package com.example.books.exceptions;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String msg){
        super(msg);
    }
}
