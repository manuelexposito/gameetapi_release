package com.salesianostriana.gameetapi.models.requests;

import com.salesianostriana.gameetapi.models.sessions.Session;
import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RequestSession extends Request {

    @ManyToOne(fetch = FetchType.LAZY)
    private Session session;

    private String hostSessionUsername;



}
