package com.salesianostriana.gameetapi.models.commercialproperty;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommercialProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private String imageUrl;

    @Builder.Default
    private String [] owners = {};

    private String description;

    private LocalTime openingHours;

    private LocalTime closingHours;

    //Latlong
    private String location;

    private boolean available;

    private int availableTables;




}
