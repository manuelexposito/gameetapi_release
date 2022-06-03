package com.salesianostriana.gameetapi.models.sessions.dto;

import com.salesianostriana.gameetapi.models.sessions.Place;
import lombok.*;

import javax.validation.constraints.Future;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSessionDto {

    private String hostNotes;

    @NonNull //@Future
    private LocalDateTime date;


    private Place placeType;
    private Long placeId;
    private Long gameId;
    @Builder.Default
    private boolean isClosed = false;

    private String latlng;

    @Builder.Default
    private List<String> playersUsername = new ArrayList<>();

    private int estimatedPlaytime;

    @Builder.Default
    private boolean available = true;

    private boolean isPrivate;

}