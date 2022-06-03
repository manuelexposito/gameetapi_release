package com.salesianostriana.gameetapi.services.interfaces;


import com.salesianostriana.gameetapi.models.commercialproperty.CommercialProperty;
import com.salesianostriana.gameetapi.models.commercialproperty.dto.CreateCommercialPropertyDto;
import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CommercialPropertyService {

    CommercialProperty save(CreateCommercialPropertyDto dto, MultipartFile image);

    CommercialProperty find(Long id);

    CommercialProperty findByLocation(String latlong);

    Page<CommercialProperty> findAll(Pageable pageable);

    Page<CommercialProperty> findAvailablePlaces(Pageable pageable);

    List<CommercialProperty> findAvailablePlaces();

    void delete(Long id);

    CommercialProperty edit(Long id, CreateCommercialPropertyDto dto);


}