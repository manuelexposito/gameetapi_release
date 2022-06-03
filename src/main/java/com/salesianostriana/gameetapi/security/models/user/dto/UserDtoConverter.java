package com.salesianostriana.gameetapi.security.models.user.dto;


import com.salesianostriana.gameetapi.repositories.RequestRepository;
import com.salesianostriana.gameetapi.repositories.UserEntityRepository;
import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDtoConverter {

    private final UserEntityRepository userEntityRepository;
    private final RequestRepository requestRepository;

    public UserDto convertUserEntityToUserDto(UserEntity user) {
        return UserDto.builder()
                .id(user.getId())
                .avatar(user.getAvatar())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .birthdate(user.getBirthdate())
                .biography(user.getBiography())
                .email(user.getEmail())
                .latlng(user.getLatlong())
                .interests(user.getInterests())
                .role(user.getRole().name())
                .friendsNumber(userEntityRepository.getNumberOfFriends(user.getId()))
                .sessionsPlayedNumber(userEntityRepository.getNumberOfSessionsPlayed(user.getId()))
                .sessionsHostedNumber(userEntityRepository.getNumberOfSessionsHosted(user.getUsername()))
                .build();


    }

    public UserDetailsDto convertUserEntityToUserDetailsDto(UserEntity user, UUID currentUserId, String currentUsername) {

        FriendshipStatus status = FriendshipStatus.NONE;

        if(requestRepository.checkIfFriendshipExists(user.getId(), currentUserId) == 1){
            status = FriendshipStatus.FRIEND;
        } else if(requestRepository.findRequestFriendshipByCurrentUser(currentUsername, user.getUsername()).isPresent()){
            status = FriendshipStatus.REQUESTED;
        } else if(requestRepository.findRequestFriendshipByAnotherUser(currentUsername, user.getUsername()).isPresent()){
            status = FriendshipStatus.REQUESTING;

        } else {
            status = FriendshipStatus.NONE;
        }

        return UserDetailsDto.builder()
                .id(user.getId())
                .avatar(user.getAvatar())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .birthdate(user.getBirthdate())
                .biography(user.getBiography())
                .email(user.getEmail())
                .latlng(user.getLatlong())
                .interests(user.getInterests())
                .role(user.getRole().name())
                .friendsInCommon(userEntityRepository.getFriendsInCommon(user.getId(), currentUserId)
                        .stream()
                        .map(x -> x.getUsername())
                        .collect(Collectors.toList()))
                .friendsNumber(userEntityRepository.getNumberOfFriends(user.getId()))
                .sessionsPlayedNumber(userEntityRepository.getNumberOfSessionsPlayed(user.getId()))
                .sessionsHostedNumber(userEntityRepository.getNumberOfSessionsHosted(user.getUsername()))
                .status(status)
                //.isFriend(requestRepository.checkIfFriendshipExists(user.getId(), currentUserId) == 1 ? true : false)
                //.isFriendshipRequest(requestRepository.findRequestFriendshipByUsers(user.getUsername(), currentUsername).isPresent())
                .build();


    }



}
