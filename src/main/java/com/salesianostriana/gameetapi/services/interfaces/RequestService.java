package com.salesianostriana.gameetapi.services.interfaces;

import com.salesianostriana.gameetapi.models.requests.Request;
import com.salesianostriana.gameetapi.models.requests.RequestFriendship;
import com.salesianostriana.gameetapi.models.requests.RequestSession;
import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface RequestService {

    Optional<RequestSession> findRequestSessionBySessionIdAndUser(Long idSession, UUID userId);

    RequestSession sendSessionInvitation(UserEntity currentUser, Long sessionId, String invitedUsername);

    RequestFriendship sendFriendshipRequest(UserEntity currentUser, String username);

    void acceptFriendship(String currentUserName, String username);


    void rejectFriendship(String currentUserName, String username);

    Page<Request> fetchAllRequests(UserEntity currentUser, Pageable pageable);

    void acceptSessionInvitation(Long sessionId, UUID id);

    void rejectSessionInvitation(Long sessionId, UUID id);

    //TODO: Ask for invitation

    //TODO: accept New User

    //TODO: reject New User





}
