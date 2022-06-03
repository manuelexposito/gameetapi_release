package com.salesianostriana.gameetapi.controllers;

import com.salesianostriana.gameetapi.models.sessions.Place;
import com.salesianostriana.gameetapi.models.sessions.Session;
import com.salesianostriana.gameetapi.models.sessions.dto.CreateSessionDto;
import com.salesianostriana.gameetapi.models.sessions.dto.SessionDto;
import com.salesianostriana.gameetapi.models.sessions.dto.SessionDtoConverter;
import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import com.salesianostriana.gameetapi.services.interfaces.SessionService;
import com.salesianostriana.gameetapi.utils.pagination.PaginationLinksUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RequestMapping("/api/session")
@RestController
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final SessionDtoConverter converter;
    private final PaginationLinksUtils paginationLinksUtils;

    @PostMapping()
    public ResponseEntity<SessionDto> createSession(@Valid @RequestBody CreateSessionDto dto, @AuthenticationPrincipal UserEntity currentUser) {

        Session newSession = sessionService.save(dto, currentUser.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(converter.convertToSessionDto(newSession, currentUser));

    }

    @GetMapping("/public")
    public ResponseEntity<Page<SessionDto>> fetchPublicSessions(@PageableDefault(size=10, page=0)Pageable pageable, HttpServletRequest request, final Optional<Integer> playersNum,
                                                                final Optional<String> gameName,
                                                                final Optional<Place> placeType, @AuthenticationPrincipal UserEntity currentUser){

        Page<SessionDto> sessionDto = sessionService.findPublicSessions(pageable, playersNum, gameName, placeType).map(s -> converter.convertToSessionDto(s, currentUser));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok().header("link",
                        paginationLinksUtils.createLinkHeader(sessionDto, uriBuilder))
                .body(sessionDto);
    }


    @GetMapping("/me")
    public ResponseEntity<Page<SessionDto>> fetchMyNextSessions(@AuthenticationPrincipal UserEntity currentUser, @PageableDefault(size=10, page=0)Pageable pageable, HttpServletRequest request){

        Page<SessionDto> sessionDto = sessionService.findMyNextSessions(pageable, currentUser).map(s -> converter.convertToSessionDto(s, currentUser));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok().header("link",
                        paginationLinksUtils.createLinkHeader(sessionDto, uriBuilder))
                .body(sessionDto);

    }

    @GetMapping("/last-played/{username}")
    public ResponseEntity<Page<SessionDto>> fetchUserLastSessions(@PathVariable String username, @PageableDefault(size=10, page=0)Pageable pageable, HttpServletRequest request, @AuthenticationPrincipal UserEntity currentUser){

        Page<SessionDto> sessionDto = sessionService.findLastPlayedSessions(username, pageable).map(s -> converter.convertToSessionDto(s, currentUser));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok().header("link",
                        paginationLinksUtils.createLinkHeader(sessionDto, uriBuilder))
                .body(sessionDto);

    }



    @GetMapping("/{session_id}")
    public SessionDto fetchSessionById(@PathVariable("session_id") Long sessionId, @AuthenticationPrincipal UserEntity currentUser){

        return converter.convertToSessionDto(sessionService.find(sessionId, currentUser), currentUser);
    }

}
