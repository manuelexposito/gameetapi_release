package com.salesianostriana.gameetapi.security.models.user.dto;

public enum FriendshipStatus {

    FRIEND,
    REQUESTING, //Cuando el principal es solicitado por otro usuario
    REQUESTED, // Cuando el usuario es solicitado por el principal
    NONE
}
