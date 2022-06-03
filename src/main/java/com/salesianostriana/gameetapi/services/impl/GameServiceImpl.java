package com.salesianostriana.gameetapi.services.impl;

import com.salesianostriana.gameetapi.errors.exceptions.entitynotfound.SingleEntityNotFoundException;
import com.salesianostriana.gameetapi.models.games.Game;
import com.salesianostriana.gameetapi.models.games.dto.CreateGameDto;
import com.salesianostriana.gameetapi.repositories.GameRepository;
import com.salesianostriana.gameetapi.services.interfaces.GameService;
import com.salesianostriana.gameetapi.services.interfaces.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository repository;
    private final StorageService storageService;

    @Override
    public Game save(CreateGameDto game, MultipartFile file) {

        String uri = storageService.uploadFile(file);
        Game newGame = Game.builder()
                .available(true)
                .name(game.getName())
                .description(game.getDescription())
                .imageUrl(uri)
                .maxAge(game.getMaxAge())
                .minAge(game.getMinAge())
                .maxPlayers(game.getMaxPlayers())
                .minPlayers(game.getMinPlayers())
                .maxPlaytime(game.getMaxPlaytime())
                .minPlaytime(game.getMinPlaytime())
                .votes(game.getVotes())
                .build();

        return repository.save(newGame);
    }

    @Override
    public Game find(Long id) {

        Optional<Game> wantedGame = repository.findById(id);

        if(wantedGame.isPresent()){
            return repository.findById(id).get();
        }else throw new SingleEntityNotFoundException(Game.class, id);

    }

    @Override
    public Page<Game> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Game> findAllAvailable(Pageable pageable) {
        return repository.findAllAvailable(pageable);
    }

    @Override
    public List<Game> findAllAvailable() {
        return repository.findAllAvailable();
    }

    @Override
    public Game edit(Long id, CreateGameDto game) {


        Optional<Game> wantedGame = repository.findById(id);

        if(wantedGame.isPresent()){

            Game foundGame = wantedGame.get();

            foundGame = Game.builder()
                    .id(id)
                    .available(true)
                    .name(game.getName())
                    .description(game.getDescription())
                    .imageUrl(game.getImageUrl())
                    .maxAge(game.getMaxAge())
                    .minAge(game.getMinAge())
                    .maxPlayers(game.getMaxPlayers())
                    .minPlayers(game.getMinPlayers())
                    .maxPlaytime(game.getMaxPlaytime())
                    .minPlaytime(game.getMinPlaytime())
                    .votes(game.getVotes())
                    .build();

            return repository.save(foundGame);
        }else throw new SingleEntityNotFoundException(Game.class, id);
    }

    @Override
    public void remove(Long id) {

        repository.deleteById(id);
    }
}