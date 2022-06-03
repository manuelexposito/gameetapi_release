package com.salesianostriana.gameetapi.services.impl;


import com.salesianostriana.gameetapi.errors.exceptions.entitynotfound.SingleEntityNotFoundException;
import com.salesianostriana.gameetapi.models.commercialproperty.CommercialProperty;
import com.salesianostriana.gameetapi.models.commercialproperty.dto.CommercialPropertyDto;
import com.salesianostriana.gameetapi.models.commercialproperty.dto.CreateCommercialPropertyDto;
import com.salesianostriana.gameetapi.repositories.CommercialPropertyRepository;
import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import com.salesianostriana.gameetapi.security.models.user.role.UserRole;
import com.salesianostriana.gameetapi.services.interfaces.CommercialPropertyService;
import com.salesianostriana.gameetapi.services.interfaces.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommercialPropertyServiceImpl implements CommercialPropertyService {

    private final CommercialPropertyRepository repository;
    private final StorageService storageService;

    @Override
    public CommercialProperty save(CreateCommercialPropertyDto dto, MultipartFile image) {
        Optional<CommercialProperty> checkProperty = repository.findPlaceByLocation(dto.getLocation());

        if(checkProperty.isEmpty()){
            String uri = storageService.uploadFile(image);
            CommercialProperty newCommerce = CommercialProperty.builder()
                    .name(dto.getName())
                    .phone(dto.getPhone())
                    .description(dto.getDescription())
                    .location(dto.getLocation())
                    .owners(dto.getOwners())
                    .availableTables(dto.getAvailableTables())
                    .openingHours(dto.getOpeningHours())
                    .closingHours(dto.getClosingHours())
                    .imageUrl(uri)
                    .available(dto.isAvailable())
                    .build();


            return repository.save(newCommerce);
        } else throw new EntityExistsException();



    }

    @Override
    public CommercialProperty find(Long id) {

        Optional<CommercialProperty> commerce = repository.findById(id);

        if (commerce.isPresent()) {
            return commerce.get();
        } else throw new SingleEntityNotFoundException(CommercialProperty.class, id);


    }

    @Override
    public CommercialProperty findByLocation(String latlong) {
        Optional<CommercialProperty> commerce = repository.findPlaceByLocation(latlong);

        if (commerce.isPresent()) {
            return commerce.get();
        } else throw new SingleEntityNotFoundException(CommercialProperty.class);


    }

    @Override
    public Page<CommercialProperty> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<CommercialProperty> findAvailablePlaces(Pageable pageable) {
        return repository.findAvailablePlaces(pageable);
    }

    @Override
    public List<CommercialProperty> findAvailablePlaces() {
        return repository.findAvailablePlaces();
    }


    @Override
    public void delete(Long id) {

        Optional<CommercialProperty> commerceOp = repository.findById(id);
        if (commerceOp.isPresent()) {

            CommercialProperty commerce = commerceOp.get();
            storageService.deleteFile(commerce.getImageUrl());
            repository.delete(commerce);

        } else throw new SingleEntityNotFoundException(CommercialProperty.class, id);
    }

    //TODO: Gestionar el error si ya existe un comercial disponible con la misma latitud / longitud
    @Override
    public CommercialProperty edit(Long id, CreateCommercialPropertyDto dto) {

        Optional<CommercialProperty> commerceOp = repository.findById(id);

        if (commerceOp.isPresent()) {
            CommercialProperty commerce = commerceOp.get();

            commerce = CommercialProperty.builder()
                    .id(id)
                    .name(dto.getName())
                    .phone(dto.getPhone())
                    .description(dto.getDescription())
                    .location(dto.getLocation())
                    .owners(dto.getOwners())
                    .availableTables(dto.getAvailableTables())
                    .openingHours(dto.getOpeningHours())
                    .closingHours(dto.getClosingHours())
                    .imageUrl(commerce.getImageUrl())
                    .available(dto.isAvailable())
                    .build();

            return repository.save(commerce);

        } else throw new SingleEntityNotFoundException(CommercialProperty.class, id);

    }
}