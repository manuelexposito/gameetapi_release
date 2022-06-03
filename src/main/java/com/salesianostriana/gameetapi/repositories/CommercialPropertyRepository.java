package com.salesianostriana.gameetapi.repositories;

import com.salesianostriana.gameetapi.models.commercialproperty.CommercialProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommercialPropertyRepository extends JpaRepository<CommercialProperty, Long> {



    @Query(value = """
            SELECT *
            FROM COMMERCIAL_PROPERTY CP
            WHERE CP.available = True
            """, nativeQuery = true)
    Page<CommercialProperty> findAvailablePlaces(Pageable pageable);


    @Query(value = """
            SELECT *
            FROM COMMERCIAL_PROPERTY CP
            WHERE CP.available = True
            """, nativeQuery = true)
    List<CommercialProperty> findAvailablePlaces();

    @Query(value = """
            SELECT *
            FROM COMMERCIAL_PROPERTY CP
            WHERE CP.location = :latlong
            AND CP.available = True
            LIMIT 1
            """,nativeQuery = true)
    Optional<CommercialProperty> findPlaceByLocation(@Param("latlong")String latlong);
}