package com.salesianostriana.gameetapi.repositories;

import com.salesianostriana.gameetapi.models.sessions.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query(value = """
            SELECT *
            FROM SESSION S
            WHERE S.is_private = False
            """, nativeQuery = true)
    Page<Session> findPublicSessions(Pageable pageable, Specification<Session> allArgs);


    @Query(value = """
            SELECT *
            FROM SESSION S
            WHERE S.host = :username
            """, nativeQuery = true)
    Page<Session> findSessionsByHost(@Param("username")String username, Pageable pageable);

    @Query(value = """
            SELECT *
            FROM COMMERCIAL_PROPERTY cp
            JOIN FETCH SESSION s
            WHERE s.place = :commercialId
            """, nativeQuery = true)
    List<Session> findSessionsByCommercialProperty(@Param("commercialId") Long id);


    @Query(value = """
            SELECT *
            FROM SESSION s
            JOIN FETCH SESSION_PLAYERS sp ON (s.id = sp.session_id)
            WHERE sp.player_id = :id
            """,nativeQuery = true)
    Page<Session> findSessionsByUser(@Param("id") String id, Pageable pageable);

    @Query(value = """
            SELECT *
            FROM SESSION s
            JOIN SESSION_PLAYERS sp ON (s.id = sp.session_id)
            WHERE sp.player_id = :id_user
            AND sp.session_id = :id_session
            """,nativeQuery = true)
    Optional<Session> findSessionByIdAndUserId(@Param("id_session") Long idSession, @Param("id_user") UUID idUser);


    @Query(value = """
            SELECT * FROM SESSION s
            WHERE s.date >= current_timestamp
            AND :id
            IN (SELECT player_id FROM SESSION_PLAYERS WHERE player_id = :id AND session_id = s.id)
            ORDER BY s.date
            """, nativeQuery = true)
    Page<Session> findMyNextSessions(@Param("id")UUID id,Pageable pageable);


    @Query(value = """
            SELECT *
            FROM SESSION s
            JOIN SESSION_PLAYERS sp ON (s.id = sp.session_id)
            WHERE sp.player_id = :id
            AND s.date < current_timestamp
            ORDER BY s.date DESC
            """, nativeQuery = true)
    Page<Session> findLastPlayedSessions(@Param("id") UUID userId, Pageable pageable);


    Optional<Session> findById(Long sessionId);


}