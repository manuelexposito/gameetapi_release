package com.salesianostriana.gameetapi.models.requests;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@Entity
public class RequestFriendship extends Request{

}
