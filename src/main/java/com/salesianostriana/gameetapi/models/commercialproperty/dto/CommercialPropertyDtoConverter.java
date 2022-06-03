package com.salesianostriana.gameetapi.models.commercialproperty.dto;

import com.salesianostriana.gameetapi.models.commercialproperty.CommercialProperty;
import com.salesianostriana.gameetapi.services.interfaces.CommercialPropertyService;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class CommercialPropertyDtoConverter {

    public CommercialPropertyDto commercialPropertyToDto(CommercialProperty commerce){


        return CommercialPropertyDto.builder()
                .id(commerce.getId())
                .name(commerce.getName())
                .available(commerce.isAvailable())
                .availableTables(commerce.getAvailableTables())
                .closingHours(commerce.getClosingHours())
                .description(commerce.getDescription())
                .imageUrl(commerce.getImageUrl())
                .location(commerce.getLocation())
                .openingHours(commerce.getOpeningHours())
                .owners(commerce.getOwners())
                .phone(commerce.getPhone())
                .build();

    }


}