package com.salesianostriana.gameetapi.models.sessions.dto;

import com.salesianostriana.gameetapi.models.sessions.Place;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionDto {

    private Long id;
    private String hostName, hostNotes;

    private LocalDateTime date;


    private String placeName;
    private String location;
    private String placeType;
    private String gameName;
    private List<String> players;
    private String gameImage;
    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    private int numPlayers;

    private int estimatedPlaytime;

    private boolean isPrivate, isClosed;

}