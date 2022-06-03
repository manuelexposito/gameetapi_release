package com.salesianostriana.gameetapi.models.requests;

import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public abstract class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String petition;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userRequested;

    private String requestedValue;
    private String imgResource;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
