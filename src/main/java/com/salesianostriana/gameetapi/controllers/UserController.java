package com.salesianostriana.gameetapi.controllers;

import com.salesianostriana.gameetapi.models.sessions.dto.SessionDto;
import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import com.salesianostriana.gameetapi.security.models.user.dto.UserDetailsDto;
import com.salesianostriana.gameetapi.security.models.user.dto.UserDto;
import com.salesianostriana.gameetapi.security.models.user.dto.UserDtoConverter;
import com.salesianostriana.gameetapi.services.interfaces.UserEntityService;
import com.salesianostriana.gameetapi.utils.pagination.PaginationLinksUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserEntityService userEntityService;
    private final UserDtoConverter userDtoConverter;
    private final PaginationLinksUtils paginationLinksUtils;

    @GetMapping
    public ResponseEntity<Page<UserDto>> fetchAllUsers(@PageableDefault(size=10, page=0)Pageable pageable, HttpServletRequest request, @AuthenticationPrincipal UserEntity currentUser) {


        Page<UserDto> userDto = userEntityService.fetchUsers(currentUser, pageable).map(userDtoConverter::convertUserEntityToUserDto);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok().header("link",
                        paginationLinksUtils.createLinkHeader(userDto, uriBuilder))
                .body(userDto);}


    @GetMapping("/playerspic/{usernameList}")
    public List<String> fetchPlayersAvatars(@PathVariable List<String> usernameList){

        return userEntityService.findAvatarsFromSession(usernameList);


    }

    @GetMapping("/{username}")
    public UserDetailsDto fetchUserByUsername(@PathVariable String username, @AuthenticationPrincipal UserEntity currentUser){
        UserEntity user = userEntityService.findByUsername(username);

        return userDtoConverter.convertUserEntityToUserDetailsDto(user, currentUser.getId(), currentUser.getUsername());
    }

    @GetMapping("/friends")
    public ResponseEntity<Page<UserDetailsDto>> fetchMyFriends(@PageableDefault(size=10, page=0)Pageable pageable, HttpServletRequest request, @AuthenticationPrincipal UserEntity currentUser) {


        Page<UserDetailsDto> userDto = userEntityService.getMyFriends(currentUser, pageable).map(x -> userDtoConverter.convertUserEntityToUserDetailsDto(x, currentUser.getId(), currentUser.getUsername()));

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok().header("link",
                        paginationLinksUtils.createLinkHeader(userDto, uriBuilder))
                .body(userDto);}


}
