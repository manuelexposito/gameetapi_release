package com.salesianostriana.gameetapi.services.interfaces;

import com.salesianostriana.gameetapi.models.games.Game;
import com.salesianostriana.gameetapi.models.games.dto.CreateGameDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GameService {

    Game save(CreateGameDto game, MultipartFile file);

    Game find(Long id);

    Page<Game> findAll(Pageable pageable);

    Page<Game> findAllAvailable(Pageable pageable);

    List<Game> findAllAvailable();

    Game edit(Long id, CreateGameDto gameDto);

    void remove(Long id);



}