package com.salesianostriana.gameetapi.models.commercialproperty.dto;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommercialPropertyDto {

    private Long id;
    private String name, phone, description, location, imageUrl;
    @Builder.Default
    private String [] owners = {};
    private boolean available;
    private int availableTables;
    private LocalTime openingHours;
    private LocalTime closingHours;


}
