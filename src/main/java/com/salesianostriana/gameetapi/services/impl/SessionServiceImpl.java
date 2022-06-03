package com.salesianostriana.gameetapi.services.impl;

import com.salesianostriana.gameetapi.errors.exceptions.entitynotfound.SingleEntityNotFoundException;
import com.salesianostriana.gameetapi.errors.exceptions.forbidden.PrivateSessionException;
import com.salesianostriana.gameetapi.models.commercialproperty.CommercialProperty;
import com.salesianostriana.gameetapi.models.games.Game;
import com.salesianostriana.gameetapi.models.requests.Request;
import com.salesianostriana.gameetapi.models.requests.RequestSession;
import com.salesianostriana.gameetapi.models.sessions.Place;
import com.salesianostriana.gameetapi.models.sessions.Session;
import com.salesianostriana.gameetapi.models.sessions.dto.CreateSessionDto;
import com.salesianostriana.gameetapi.repositories.SessionRepository;
import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import com.salesianostriana.gameetapi.security.models.user.role.UserRole;
import com.salesianostriana.gameetapi.services.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.criteria.Predicate;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository repository;
    private final GameService gameService;
    private final UserEntityService userEntityService;
    private final CommercialPropertyService commercialPropertyService;
    private final RequestService requestService;

    @Override
    public Session save(CreateSessionDto dto, String username) {

        Session newSession = new Session();
        Game game = gameService.find(dto.getGameId());

        UserEntity currentUser = userEntityService.findByUsername(username);


        if (dto.getEstimatedPlaytime() <= 0) {
            //Si el tiempo estimado 0, se hace una media con el tiempo descrito en el juego
            newSession.setEstimatedPlaytime((game.getMaxPlaytime() + game.getMinPlaytime()) / 2);
        } else {
            newSession.setEstimatedPlaytime(dto.getEstimatedPlaytime());
        }


        newSession.setDate(dto.getDate());
        newSession.setHost(currentUser.getUsername());
        newSession.setGame(game);
        newSession.setGameName(game.getName());
        newSession.setPrivate(dto.isPrivate());
        newSession.setHostNotes(dto.getHostNotes());
        newSession.setPlaceType(dto.getPlaceType());


        switch (dto.getPlaceType()) {
            case HOME:

                newSession.setLocation(dto.getLatlng().isEmpty() ? currentUser.getLatlong() : dto.getLatlng());
                newSession.setPlace(null);

                break;
            case COMMERCIAL:
                CommercialProperty searchProp = commercialPropertyService.find(dto.getPlaceId());
                newSession.setLocation(searchProp.getLocation());
                newSession.setPlace(searchProp);
                break;

            default:
                newSession.setLocation(null);
                newSession.setPlace(null);
        }

        repository.save(newSession);

        for (String playerUsername : dto.getPlayersUsername()) {

            UserEntity player = userEntityService.findByUsername(playerUsername);
            requestService.sendSessionInvitation(currentUser, newSession.getId(), player.getUsername());

        }

        currentUser.addToSession(newSession);
        userEntityService.save(currentUser);
        return newSession;
    }

    @Override
    public Page<Session> findPublicSessions(Pageable pageable,
                                            final Optional<Integer> numPlayers,
                                            final Optional<String> gameName,
                                            final Optional<Place> placeType) {


        Specification<Session> specNumPlayers = new Specification<Session>() {
            @Override
            public Predicate toPredicate(Root<Session> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (numPlayers.isPresent()) {
                    return criteriaBuilder.lessThanOrEqualTo(root.get("numPlayers"), numPlayers.get());
                } else {

                    return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                }
            }
        };

        Specification<Session> specGameName = new Specification<Session>() {
            @Override
            public Predicate toPredicate(Root<Session> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (gameName.isPresent()) {

                    return criteriaBuilder.like(criteriaBuilder.lower(root.get("gameName")), "%" + gameName.get() + "%");
                } else {

                    return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                }
            }
        };

        Specification<Session> specPlaceType = new Specification<Session>() {
            @Override
            public Predicate toPredicate(Root<Session> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (placeType.isPresent()) {

                    return criteriaBuilder.like(criteriaBuilder.upper(root.get("placeType")), "%" + placeType.get() + "%");
                } else {

                    return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                }
            }
        };


        Specification<Session> allArgs = specGameName.and(specNumPlayers.and(specPlaceType));

        return repository.findPublicSessions(pageable, allArgs);
    }

    @Override
    public Page<Session> findMyNextSessions(Pageable pageable, UserEntity currentUser) {
        return repository.findMyNextSessions(currentUser.getId(), pageable);
    }

    @Override
    public Page<Session> findMyHostedSessions(Pageable pageable) {
        return null;
    }

    @Override
    public Session edit(CreateSessionDto dto) {
        return null;
    }

    @Override
    public Session find(Long id, UserEntity user) {

        //Formas de ver la sesión
        Optional<Session> wantedSession = repository.findById(id);

        if (wantedSession.isPresent()) {

            Session foundSession = wantedSession.get();

            Optional<RequestSession> wantedRequest = requestService.findRequestSessionBySessionIdAndUser(foundSession.getId(), user.getId());


            if (foundSession.isPrivate()) {

                if (wantedRequest.isPresent() || repository.findSessionByIdAndUserId(foundSession.getId(), user.getId()).isPresent() || user.getRole().equals(UserRole.ADMIN)) {
                    return foundSession;
                } else throw new PrivateSessionException();

            } else {
                return foundSession;
            }
/*

            //Si es privada
            if (foundSession.isPrivate() && user.getRole().equals(UserRole.USER) && wantedRequest.isEmpty() && !foundSession.getPlayers().contains(user) )
                throw new PrivateSessionException();


//Si es pública
            if (!foundSession.isPrivate() || wantedRequest.isPresent() || foundSession.getPlayers().contains(user) || user.getRole().equals(UserRole.ADMIN)) {
                return foundSession;
            }
*/


        } else throw new SingleEntityNotFoundException(Session.class);

    }

    @Override
    public Page<Session> findLastPlayedSessions(String username, Pageable pageable) {

        UserEntity user = userEntityService.findByUsername(username);


        return repository.findLastPlayedSessions(user.getId(),  pageable);
    }
}