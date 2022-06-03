package com.salesianostriana.gameetapi.controllers;

import com.salesianostriana.gameetapi.models.commercialproperty.dto.CommercialPropertyDto;
import com.salesianostriana.gameetapi.models.games.Game;
import com.salesianostriana.gameetapi.models.games.dto.CreateGameDto;
import com.salesianostriana.gameetapi.models.games.dto.GameDto;
import com.salesianostriana.gameetapi.models.games.dto.GameDtoConverter;
import com.salesianostriana.gameetapi.services.interfaces.GameService;
import com.salesianostriana.gameetapi.utils.pagination.PaginationLinksUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameDtoConverter gameDtoConverter;
    private final GameService gameService;
    private final PaginationLinksUtils paginationLinksUtils;





    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<GameDto> createGame(@Valid @RequestPart("body") CreateGameDto gameDto, @RequestPart("file") MultipartFile file) {

        GameDto game = gameDtoConverter.gameToGameDto(gameService.save(gameDto, file));

        return ResponseEntity.status(HttpStatus.CREATED).body(game);


    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<GameDto>> fetchAllGames(@PageableDefault(size=10, page=0)Pageable pageable, HttpServletRequest request) {

        Page<GameDto> gameDto = gameService.findAll(pageable).map(gameDtoConverter::gameToGameDto);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok().header("link",
                        paginationLinksUtils.createLinkHeader(gameDto, uriBuilder))
                .body(gameDto);

    }

    //Para seleccionar en front a la hora de crear una sesion
    @GetMapping("/selection")
    public List<GameDto> fetchAvailableGames(){

        return gameService.findAllAvailable().stream().map(gameDtoConverter::gameToGameDto).collect(Collectors.toList());
    }


    @GetMapping("/available")
    public ResponseEntity<Page<GameDto>> fetchAvailableGames(@PageableDefault(size=10, page=0)Pageable pageable, HttpServletRequest request) {

        Page<GameDto> gameDto = gameService.findAllAvailable(pageable).map(gameDtoConverter::gameToGameDto);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok().header("link",
                        paginationLinksUtils.createLinkHeader(gameDto, uriBuilder))
                .body(gameDto);

    }

    @GetMapping("/{id}")
    public GameDto fetchOneGame(@PathVariable Long id) {

        Game game = gameService.find(id);
        return gameDtoConverter.gameToGameDto(game);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public GameDto editGame(@Valid @RequestBody CreateGameDto gameDto, @PathVariable Long id) {
        Game game = gameService.edit(id, gameDto);
        return gameDtoConverter.gameToGameDto(game);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGame(@PathVariable Long id){
        gameService.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}