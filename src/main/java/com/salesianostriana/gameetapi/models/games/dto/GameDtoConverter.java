package com.salesianostriana.gameetapi.models.games.dto;

import com.salesianostriana.gameetapi.models.games.Game;
import org.springframework.stereotype.Component;

@Component
public class GameDtoConverter {

    public GameDto gameToGameDto(Game game){

        return GameDto.builder()
                .id(game.getId())
                .name(game.getName())
                .minPlayers(game.getMinPlayers())
                .maxPlayers(game.getMaxPlayers())
                .minPlaytime(game.getMinPlaytime())
                .maxPlaytime(game.getMaxPlaytime())
                .minAge(game.getMinAge())
                .maxAge(game.getMaxAge())
                .description(game.getDescription())
                .imageUrl(game.getImageUrl())
                .votes(game.getVotes())
                .build();

    }


}