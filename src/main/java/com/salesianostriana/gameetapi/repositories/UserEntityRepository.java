package com.salesianostriana.gameetapi.repositories;

import com.salesianostriana.gameetapi.security.models.user.UserEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {



    Optional<UserEntity> findFirstByUsername(String username);

    Optional<UserEntity> findFirstByEmail(String email);

    
    @Query(value = """
            SELECT DISTINCT u.* FROM USER_ENTITY u JOIN FRIENDSHIP f ON (u.id = f.user2_id)
            WHERE u.id IN
            (SELECT user2_id FROM USER_ENTITY uu JOIN FRIENDSHIP f ON (uU.id = f.user1_id) WHERE F.user1_id = :current_user)
            AND u.id IN
            (SELECT user2_id FROM USER_ENTITY uu JOIN FRIENDSHIP f ON (uU.id = f.user1_id) WHERE F.user1_id = :requested_user)
                        
            """, nativeQuery = true)
    List<UserEntity> getFriendsInCommon(@Param("requested_user") UUID user, @Param("current_user") UUID currentUser);

    @Query(value = """
            SELECT COUNT(f.user2_id)
            FROM FRIENDSHIP f WHERE
            f.user1_id = :id
            """, nativeQuery = true)
    Long getNumberOfFriends(@Param("id") UUID userId);


    @Query(value= """
            SELECT COUNT(s.*)
            FROM SESSION_PLAYERS sp
            JOIN SESSION s ON (s.id = sp.session_id)
            WHERE sp.player_id = :id
            AND s.date < current_timestamp
            """, nativeQuery = true)
    Long getNumberOfSessionsPlayed(@Param("id") UUID userId);

    @Query(value = """
            SELECT COUNT(*) FROM SESSION s
            WHERE s.host = :username
            AND date < current_timestamp
            """, nativeQuery = true)
    Long getNumberOfSessionsHosted(@Param("username") String username);


    @Query(value= """
            SELECT *
            FROM USER_ENTITY u
            JOIN FETCH SESSION_PLAYERS sp ON (u.id = sp.player_id)
            WHERE sp.session_id = :id
            """, nativeQuery = true)
    Page<UserEntity> findUsersBySessionId(@Param("id") Long id, Pageable pageable);

    @Query(value = """
            SELECT *
            FROM USER_ENTITY u
            WHERE u.role LIKE 'USER'
            """, nativeQuery = true)
    Page<UserEntity> findUsers(Pageable pageable);

    @Query(value ="""
            SELECT *
            FROM USER_ENTITY u
            JOIN FRIENDSHIP f ON (u.id = f.user1_id)
            WHERE f.user2_id = :user_id
            
            """, nativeQuery = true)
    Page<UserEntity> findMyFriends(@Param("user_id") UUID currentUserId, Pageable pageable);

    @Query(value = """
            SELECT avatar
            FROM user_entity u
            WHERE u.username in :usernameList
            """, nativeQuery = true)
    List<String> findAvatarFromSession(@Param("usernameList") List<String> username);


}
