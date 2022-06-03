package com.salesianostriana.gameetapi.services.interfaces;

import com.salesianostriana.gameetapi.models.interests.Interest;

import java.util.List;

public interface InterestsService {

    Interest save();

    List<Interest> findAll();
    //TODO: Hacer la consulta
    List<Interest> findAllByUser(String username);

}
