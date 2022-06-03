package com.salesianostriana.gameetapi.models.sessions;

import com.salesianostriana.gameetapi.models.commercialproperty.CommercialProperty;
import com.salesianostriana.gameetapi.models.games.Game;
import com.salesianostriana.gameetapi.models.sessions.dto.SessionStatus;
import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String host;

    private LocalDateTime date;
    private String hostNotes;

    private boolean isPrivate;

    @ManyToOne(fetch = FetchType.LAZY)
    private CommercialProperty place;

    @Enumerated(EnumType.STRING)
    private Place placeType;

    private String location;

    private boolean isClosed;

    @ManyToOne(fetch = FetchType.LAZY)
    private Game game;
    private String gameName;

    private int estimatedPlaytime;


    @Builder.Default
    @ManyToMany(mappedBy = "sessions")
    private List<UserEntity> players = new ArrayList<>();


}