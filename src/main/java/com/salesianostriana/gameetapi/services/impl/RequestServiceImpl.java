package com.salesianostriana.gameetapi.services.impl;

import com.salesianostriana.gameetapi.errors.exceptions.alreadyexists.FriendshipAlreadyExists;
import com.salesianostriana.gameetapi.errors.exceptions.alreadyexists.RequestAlreadyExists;
import com.salesianostriana.gameetapi.errors.exceptions.alreadyexists.UserAlreadyInvitedException;
import com.salesianostriana.gameetapi.errors.exceptions.entitynotfound.SingleEntityNotFoundException;
import com.salesianostriana.gameetapi.models.requests.Request;
import com.salesianostriana.gameetapi.models.requests.RequestFriendship;
import com.salesianostriana.gameetapi.models.requests.RequestSession;
import com.salesianostriana.gameetapi.models.sessions.Session;
import com.salesianostriana.gameetapi.repositories.RequestRepository;
import com.salesianostriana.gameetapi.repositories.SessionRepository;
import com.salesianostriana.gameetapi.repositories.UserEntityRepository;
import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import com.salesianostriana.gameetapi.services.interfaces.RequestService;
import com.salesianostriana.gameetapi.services.interfaces.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository repository;
    private final SessionRepository sessionRepository;
    private final UserEntityRepository userEntityRepository;

    @Override
    public Optional<RequestSession> findRequestSessionBySessionIdAndUser(Long idSession, UUID userId) {
        return repository.findRequestSessionBySessionIdAndUser(idSession, userId);

    }

    @Override
    public RequestSession sendSessionInvitation(UserEntity currentUser, Long sessionId, String invitedUsername) {

        //TODO: Gestionar que un HOST no pueda invitarse a sí mismo


        Optional<Session> session = sessionRepository.findById(sessionId);
        Optional<UserEntity> invitedUser = userEntityRepository.findFirstByUsername(invitedUsername);
        if (session.isPresent() && invitedUser.isPresent()) {

            UserEntity foundUser = invitedUser.get();
            Session foundSession = session.get();

            //Si el usuario tiene una invitación a esta partida o YA está en la partida
            if (repository.findRequestSessionBySessionIdAndUser(sessionId, foundUser.getId()).isPresent()
                    || foundSession.getPlayers().contains(foundUser))
                throw new UserAlreadyInvitedException();


            Request requestSession = RequestSession.builder()
                    .session(foundSession)
                    .hostSessionUsername(foundSession.getHost())
                    .petition(foundSession.getHost() + " te invita a jugar a " + foundSession.getGameName())
                    .requestedValue(foundSession.getId().toString())
                    .imgResource(foundSession.getGame().getImageUrl())
                    .build();
            repository.save(requestSession);
            foundUser.addRequest(requestSession);
            userEntityRepository.save(foundUser);

            return (RequestSession) repository.save(requestSession);

        } else throw new SingleEntityNotFoundException(Session.class);


    }

    @Override
    public RequestFriendship sendFriendshipRequest(UserEntity currentUser, String username) {

        Optional<UserEntity> anotherUser = userEntityRepository.findFirstByUsername(username);


        if (anotherUser.isPresent()) {

            UserEntity foundUser = anotherUser.get();


            if (repository.findRequestFriendshipByUsers(foundUser.getUsername(), currentUser.getUsername()).isEmpty()) {


                if (repository.checkIfFriendshipExists(currentUser.getId(), foundUser.getId()) == 0) {

                    Request requestFriendship = RequestFriendship.builder()
                            .userRequested(anotherUser.get())
                            .requestedValue(currentUser.getUsername())
                            .petition(currentUser.getUsername() + " te envió una solicitud de amistad.")
                            .imgResource(currentUser.getAvatar())
                            .build();

                    repository.save(requestFriendship);
                    foundUser.addRequest(requestFriendship);
                    userEntityRepository.save(foundUser);

                    return (RequestFriendship) repository.save(requestFriendship);

                } else throw new FriendshipAlreadyExists();

            } else throw new RequestAlreadyExists();

        } else throw new SingleEntityNotFoundException(UserEntity.class);
    }

    @Override
    public void acceptFriendship(String currentUserName, String username) {
        Optional<UserEntity> optCurrentUser = userEntityRepository.findFirstByUsername(currentUserName);
        Optional<UserEntity> anotherUser = userEntityRepository.findFirstByUsername(username);


        if (anotherUser.isPresent() && optCurrentUser.isPresent()) {
            UserEntity currentUser = optCurrentUser.get();
            UserEntity foundUser = anotherUser.get();
            Optional<RequestFriendship> request = repository.findRequestFriendshipByUsers(foundUser.getUsername(), currentUser.getUsername());

            if (request.isPresent()) {


                currentUser.addFriend(foundUser);
                currentUser.removeRequest(request.get());

                userEntityRepository.saveAll(List.of(currentUser, foundUser));

                repository.delete(request.get());
            } else throw new SingleEntityNotFoundException(Request.class);


        } else throw new SingleEntityNotFoundException(UserEntity.class);
    }

    @Override
    public void rejectFriendship(String currentUserName, String username) {
        Optional<UserEntity> optCurrentUser = userEntityRepository.findFirstByUsername(currentUserName);
        Optional<UserEntity> anotherUser = userEntityRepository.findFirstByUsername(username);


        if (anotherUser.isPresent() && optCurrentUser.isPresent()) {
            UserEntity currentUser = optCurrentUser.get();
            UserEntity foundUser = anotherUser.get();
            Optional<RequestFriendship> request = repository.findRequestFriendshipByUsers(foundUser.getUsername(), currentUser.getUsername());

            if(request.isPresent()){

                currentUser.removeRequest(request.get());
                userEntityRepository.save(currentUser);
                repository.delete(request.get());
            } else throw new SingleEntityNotFoundException(Request.class);

        }else throw new SingleEntityNotFoundException(UserEntity.class);

    }

    @Override
    public void acceptSessionInvitation(Long sessionId, UUID invitedUserId) {
        Optional<RequestSession> wantedRequest = repository.findRequestSessionBySessionIdAndUser(sessionId, invitedUserId);
        Optional<Session> wantedSession = sessionRepository.findById(sessionId);
        Optional<UserEntity> wantedUser = userEntityRepository.findById(invitedUserId);

        if (wantedRequest.isEmpty()) throw new SingleEntityNotFoundException(RequestSession.class);

        if (wantedSession.isEmpty()) throw new SingleEntityNotFoundException(Session.class);

        if (wantedUser.isEmpty()) throw new SingleEntityNotFoundException(UserEntity.class);

        UserEntity user = wantedUser.get();
        Session session = wantedSession.get();

        user.addToSession(session);
        user.removeRequest(wantedRequest.get());
        userEntityRepository.save(user);
        sessionRepository.save(session);

        repository.delete(wantedRequest.get());

    }

    @Override
    public void rejectSessionInvitation(Long sessionId, UUID id) {
        Optional<RequestSession> wantedRequest = repository.findRequestSessionBySessionIdAndUser(sessionId, id);
        Optional<Session> wantedSession = sessionRepository.findById(sessionId);
        Optional<UserEntity> wantedUser = userEntityRepository.findById(id);

        if (wantedRequest.isEmpty()) throw new SingleEntityNotFoundException(RequestSession.class);

        if (wantedSession.isEmpty()) throw new SingleEntityNotFoundException(Session.class);

        if (wantedUser.isEmpty()) throw new SingleEntityNotFoundException(UserEntity.class);

        UserEntity user = wantedUser.get();
        Session session = wantedSession.get();

        user.removeRequest(wantedRequest.get());
        userEntityRepository.save(user);
        repository.delete(wantedRequest.get());


    }



    @Override
    public Page<Request> fetchAllRequests(UserEntity currentUser, Pageable pageable) {
        return repository.fetchAllRequests(currentUser.getId(), pageable);
    }


}
