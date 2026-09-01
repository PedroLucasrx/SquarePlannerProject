package com.example.squarePlanner.exception;

public class JaExisteException extends RuntimeException {
    public JaExisteException(String mensagem){
        super(mensagem);
    }
}
