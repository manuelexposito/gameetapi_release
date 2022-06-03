package com.salesianostriana.gameetapi.services.interfaces;

import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import com.salesianostriana.gameetapi.security.models.user.dto.CreateUserDto;
import com.salesianostriana.gameetapi.security.models.user.role.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserEntityService {

    UserEntity save(CreateUserDto newUser, MultipartFile avatar, UserRole role);

    UserEntity save(UserEntity userEntity);

    UserEntity edit(CreateUserDto edit, MultipartFile avatar, UserEntity currentUser);

    UserEntity findByUsername(String username);

    List<UserEntity> saveAll(List<UserEntity> list);

    Optional<UserEntity> findById(UUID id);

    Page<UserEntity> fetchUsers(UserEntity currentUser, Pageable pageable);

    List<String> findAvatarsFromSession(List<String> usernameList);

    Page<UserEntity> getMyFriends(UserEntity currentUser, Pageable pageable);


}
