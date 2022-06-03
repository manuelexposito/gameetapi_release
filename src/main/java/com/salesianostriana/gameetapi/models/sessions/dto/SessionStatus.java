package com.salesianostriana.gameetapi.models.sessions.dto;

public enum SessionStatus {

    TIMED_OUT, //Cuando la sesion es antigua y ya no se puede participar en ella
    GOING_TO_BE, //Cuando el principal pertenece a la sesion
    REQUESTING, //Cuando el principal es invitado a la sesion
    REQUESTED, //Cuando el principal solicita entrar en la sesion
    NONE
    
    
}
