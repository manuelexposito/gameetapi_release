package com.salesianostriana.gameetapi.models.requests.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    private Long id;
    private String petition, requestedUser;
    private String requestType;
    private String requestedValue;
    private String imgResource;
    private LocalDateTime createdAt;
}
