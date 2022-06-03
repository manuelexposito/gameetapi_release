package com.salesianostriana.gameetapi.services.interfaces;

import com.salesianostriana.gameetapi.models.sessions.Place;
import com.salesianostriana.gameetapi.models.sessions.Session;
import com.salesianostriana.gameetapi.models.sessions.dto.CreateSessionDto;
import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface SessionService {

    Session save(CreateSessionDto dto, String username);

    Session edit(CreateSessionDto dto);

    Session find(Long id, UserEntity user);

    Page<Session> findLastPlayedSessions(String username, Pageable pageable);

    Page<Session> findPublicSessions(Pageable pageable,
                                     final Optional<Integer> playersNum,
                                     final Optional<String> gameName,
                                     final Optional<Place> placeType);

    Page<Session> findMyNextSessions(Pageable pageable, UserEntity currentUser);

    Page<Session> findMyHostedSessions(Pageable pageable);


}