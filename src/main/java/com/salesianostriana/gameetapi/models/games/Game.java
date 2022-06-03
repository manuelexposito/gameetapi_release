package com.salesianostriana.gameetapi.models.games;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Min(1)
    private int minPlayers, maxPlayers;

    private int minPlaytime, maxPlaytime;

    private String description;

    private String imageUrl;

    private double votes;

    private boolean available;

    @Min(0)
    private int minAge, maxAge;




}