package com.salesianostriana.gameetapi.models.commercialproperty.dto;

import com.salesianostriana.gameetapi.validation.annotations.MapsUbication;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCommercialPropertyDto {

    @NotNull @NotEmpty
    private String name, phone;

    private String description, imageUrl;

    @MapsUbication
    private String location;

    @Builder.Default
    private String [] owners = {};

    private boolean available;

    @Min(value = 0)
    private int availableTables;
    private LocalTime openingHours;
    private LocalTime closingHours;

}
