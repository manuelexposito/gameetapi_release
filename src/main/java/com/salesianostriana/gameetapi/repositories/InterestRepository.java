package com.salesianostriana.gameetapi.repositories;

import com.salesianostriana.gameetapi.models.interests.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long> {
}
