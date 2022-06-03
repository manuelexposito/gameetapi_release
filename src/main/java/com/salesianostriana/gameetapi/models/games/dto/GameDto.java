package com.salesianostriana.gameetapi.models.games.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameDto {

    private Long id;

    private int minPlayers, maxPlayers, minPlaytime, maxPlaytime, minAge, maxAge;

    private double votes;

    private String description, imageUrl, name;



}