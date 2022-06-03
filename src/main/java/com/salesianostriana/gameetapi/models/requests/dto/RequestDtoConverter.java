package com.salesianostriana.gameetapi.models.requests.dto;

import com.salesianostriana.gameetapi.models.requests.Request;
import com.salesianostriana.gameetapi.models.requests.RequestFriendship;
import com.salesianostriana.gameetapi.models.requests.RequestSession;
import org.springframework.stereotype.Component;

@Component
public class RequestDtoConverter {

    public RequestDto convertToDto(Request request){

        return RequestDto.builder()
                .id(request.getId())
                .requestedUser(request.getUserRequested().getUsername())
                .requestedValue(request.getRequestedValue())
                .requestType(request instanceof RequestFriendship ? "FRIENDSHIP" : "SESSION")
                .petition(request.getPetition())
                .imgResource(request.getImgResource())
                .createdAt(request.getCreatedAt())
                .build();

    }


}
