package com.reserva.exception;

public class SalaExistente extends RuntimeException{
    
    public SalaExistente(String message){
        super(message);
    }
}
