package com.salesianostriana.gameetapi.security.jwt;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtUserResponse {

    private String email;
    private String fullName;
    private String username;
    private String latLng;
    private String avatar;
    private String role;
    private String token;

}
