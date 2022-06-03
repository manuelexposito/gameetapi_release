package com.salesianostriana.gameetapi.errors.exceptions.entitynotfound;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(String message){
        super(message);
    }
}
