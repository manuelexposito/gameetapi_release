package com.salesianostriana.gameetapi.security.models.user.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID id;
    private String avatar, username, fullName, email, role, biography, latlng;
    private String [] interests;
    private Long friendsNumber;
    private Long sessionsPlayedNumber;
    private Long sessionsHostedNumber;
    private LocalDate birthdate;

}
