package com.salesianostriana.gameetapi.errors.exceptions.alreadyexists;

import org.springframework.beans.factory.annotation.Value;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException() {
        super();
    }
}
