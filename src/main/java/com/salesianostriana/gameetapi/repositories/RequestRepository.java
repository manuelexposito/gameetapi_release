package com.salesianostriana.gameetapi.repositories;

import com.salesianostriana.gameetapi.models.requests.Request;
import com.salesianostriana.gameetapi.models.requests.RequestFriendship;
import com.salesianostriana.gameetapi.models.requests.RequestSession;
import com.salesianostriana.gameetapi.models.sessions.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(value = """
            SELECT count(*) FROM FRIENDSHIP WHERE user1_id = :id1 AND user2_id = :id2
            """, nativeQuery = true)
    int checkIfFriendshipExists(@Param("id1") UUID firstId, @Param("id2") UUID secondId);



    @Query(value = """
            SELECT * FROM REQUEST
            WHERE session_id = :id AND user_requested_id = :user_id
            """, nativeQuery = true)
    Optional<RequestSession> findRequestSessionBySessionIdAndUser(@Param("id") Long id, @Param("user_id") UUID userId);

    @Query(value = """
            SELECT r.* FROM REQUEST r
            JOIN USER_ENTITY u ON (r.user_requested_id = u.id)
            WHERE (requested_value = :username AND u.username = :username2)
            OR (requested_value = :username2 AND u.username = :username)
            """, nativeQuery = true)
    Optional<RequestFriendship> findRequestFriendshipByUsers(@Param("username") String username, @Param("username2") String username2);

    @Query(value = """
            SELECT r.* FROM REQUEST r
            JOIN USER_ENTITY u ON (r.user_requested_id = u.id)
            WHERE requested_value = :current_user AND u.username = :another_user
            """, nativeQuery = true)
    Optional<RequestFriendship> findRequestFriendshipByCurrentUser(@Param("current_user") String username, @Param("another_user") String username2);

    @Query(value = """
            SELECT r.* FROM REQUEST r
            JOIN USER_ENTITY u ON (r.user_requested_id = u.id)
            WHERE requested_value = :another_user AND u.username = :current_user
            """, nativeQuery = true)
    Optional<RequestFriendship> findRequestFriendshipByAnotherUser(@Param("current_user") String username, @Param("another_user") String username2);



    @Query(value = """
            SELECT *
            FROM Request r
            WHERE r.user_requested_id = :id
            ORDER BY created_at DESC
            """, nativeQuery = true)
    Page<Request> fetchAllRequests(@Param("id") UUID id, Pageable pageable);

    @Query(value = """
            SELECT *
            FROM Request r
            WHERE TYPE(r) != RequestFriendship
            AND r.user_requested_id = :id
            """, nativeQuery = true)
    Page<Request> fetchSessionRequests(@Param("id") UUID id, Pageable pageable);

    @Query(value = """
            SELECT *
            FROM Request r
            WHERE TYPE(r) != RequestSession
            AND r.user_requested_id = :id
            """, nativeQuery = true)
    Page<Request> fetchFriendshipRequests(@Param("id") UUID id, Pageable pageable);

}
