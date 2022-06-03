package com.salesianostriana.gameetapi.controllers;


import com.salesianostriana.gameetapi.models.commercialproperty.CommercialProperty;
import com.salesianostriana.gameetapi.models.commercialproperty.dto.CommercialPropertyDto;
import com.salesianostriana.gameetapi.models.commercialproperty.dto.CommercialPropertyDtoConverter;
import com.salesianostriana.gameetapi.models.commercialproperty.dto.CreateCommercialPropertyDto;
import com.salesianostriana.gameetapi.models.games.dto.GameDto;
import com.salesianostriana.gameetapi.services.interfaces.CommercialPropertyService;
import com.salesianostriana.gameetapi.utils.pagination.PaginationLinksUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/property")
@RequiredArgsConstructor
public class CommercialPropertyController {


    private final CommercialPropertyDtoConverter propertyDtoConverter;
    private final CommercialPropertyService service;
    private final PaginationLinksUtils paginationLinksUtils;


    @GetMapping("/location/{latLong}")
    public CommercialPropertyDto findByLatLong(@PathVariable String latLong) {

        return propertyDtoConverter.commercialPropertyToDto(service.findByLocation(latLong));

    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<CommercialPropertyDto> createCommerce(@Valid @RequestPart("body") CreateCommercialPropertyDto dto, @RequestPart("file") MultipartFile file) {

        CommercialProperty newProperty = service.save(dto, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(propertyDtoConverter.commercialPropertyToDto(newProperty));


    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<CommercialPropertyDto>> fetchAllCommerce(@PageableDefault(size=10, page=0) Pageable pageable, HttpServletRequest request) {

        Page<CommercialPropertyDto> commerce = service.findAll(pageable).map(propertyDtoConverter::commercialPropertyToDto);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok().header("link",
                        paginationLinksUtils.createLinkHeader(commerce, uriBuilder))
                .body(commerce);

    }


    //Para seleccionar en front a la hora de crear una sesion
    @GetMapping("/selection")
    public List<CommercialPropertyDto> fetchAvailableCommerces(){

        return service.findAvailablePlaces().stream().map(propertyDtoConverter::commercialPropertyToDto).collect(Collectors.toList());
    }


    @GetMapping("/available")
    public ResponseEntity<Page<CommercialPropertyDto>> fetchAvailableCommerces(@PageableDefault(size=10, page=0) Pageable pageable, HttpServletRequest request) {

        Page<CommercialPropertyDto> commerce = service.findAvailablePlaces(pageable).map(propertyDtoConverter::commercialPropertyToDto);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok().header("link",
                        paginationLinksUtils.createLinkHeader(commerce, uriBuilder))
                .body(commerce);

    }

    @GetMapping("/{id}")
    public CommercialPropertyDto fetchOneCommerce(@PathVariable Long id) {
        CommercialProperty commerce = service.find(id);
        return propertyDtoConverter.commercialPropertyToDto(commerce);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public CommercialPropertyDto editCommerce(@Valid @RequestBody CreateCommercialPropertyDto dto, @PathVariable Long id) {

        CommercialProperty editedProperty = service.edit(id, dto);
        return propertyDtoConverter.commercialPropertyToDto(editedProperty);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommerce(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}