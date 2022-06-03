package com.salesianostriana.gameetapi.models.sessions.dto;

import com.salesianostriana.gameetapi.models.sessions.Session;
import com.salesianostriana.gameetapi.repositories.RequestRepository;
import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SessionDtoConverter {

    private final RequestRepository requestRepository;

    public SessionDto convertToSessionDto(Session session, UserEntity user){
        List<String> users = session.getPlayers().stream().map(x -> x.getUsername()).collect(Collectors.toList());

        SessionStatus status = SessionStatus.NONE;

        if (session.getDate().isBefore(LocalDateTime.now())){
            status = SessionStatus.TIMED_OUT;
        } else if(users.contains(user.getUsername())){
            status = SessionStatus.GOING_TO_BE;
        } else if(requestRepository.findRequestSessionBySessionIdAndUser(session.getId(), user.getId()).isPresent()){
            status = SessionStatus.REQUESTING;
        } else {
            status = SessionStatus.NONE;
        }
        //TODO: SessionStatus.REQUESTED cuando el principal solicite entrar.

        return SessionDto.builder()
                .id(session.getId())
                .date(session.getDate())
                .location(session.getLocation())
                .placeType(session.getPlaceType().name())
                .estimatedPlaytime(session.getEstimatedPlaytime())
                .gameImage(session.getGame().getImageUrl())
                .gameName(session.getGameName())
                .hostName(session.getHost())
                .hostNotes(session.getHostNotes())
                .isPrivate(session.isPrivate())
                .numPlayers(session.getPlayers().size())
                .isClosed(session.isClosed())
                .players(users)
                .status(status)
                .placeName(session.getPlace() != null ? session.getPlace().getName() : "" )
                .build();

    }

}
