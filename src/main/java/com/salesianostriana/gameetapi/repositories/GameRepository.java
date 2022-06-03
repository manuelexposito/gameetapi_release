package com.salesianostriana.gameetapi.repositories;

import com.salesianostriana.gameetapi.models.games.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query(value = """
            SELECT *
            FROM GAME g
            WHERE g.available = True
            """, nativeQuery = true)
    Page<Game> findAllAvailable(Pageable pageable);

    @Query(value = """
            SELECT *
            FROM GAME g
            WHERE g.available = True
            """, nativeQuery = true)
    List<Game> findAllAvailable();


}