package com.salesianostriana.gameetapi.security.controllers;

import com.salesianostriana.gameetapi.security.jwt.JwtProvider;
import com.salesianostriana.gameetapi.security.jwt.JwtUserResponse;
import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import com.salesianostriana.gameetapi.security.models.user.auth.LoginDto;
import com.salesianostriana.gameetapi.security.models.user.dto.CreateUserDto;
import com.salesianostriana.gameetapi.security.models.user.dto.UserDto;
import com.salesianostriana.gameetapi.security.models.user.dto.UserDtoConverter;
import com.salesianostriana.gameetapi.security.models.user.role.UserRole;
import com.salesianostriana.gameetapi.services.interfaces.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Log
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private  final UserEntityService userEntityService;
    private final UserDtoConverter userDtoConverter;




    @PostMapping("/registeradminfortesting")
    public ResponseEntity<UserDto> registerAdmin(@RequestPart("body") CreateUserDto admin, @RequestPart("file") MultipartFile avatar){

        UserEntity saved = userEntityService.save(admin, avatar, UserRole.ADMIN);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.convertUserEntityToUserDto(saved));
    }




    @PostMapping("/register")
    public ResponseEntity<UserDto> signup(@Valid @RequestPart("body") CreateUserDto newUser, @RequestPart("file") MultipartFile avatar){


        UserEntity saved = userEntityService.save(newUser, avatar, UserRole.USER);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.convertUserEntityToUserDto(saved));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register/admin")
    public ResponseEntity<UserDto> signupAdmin(@Valid @RequestPart("body") CreateUserDto newUser, @RequestPart("file") MultipartFile avatar,
                                               @AuthenticationPrincipal UserEntity currentUser){


        UserEntity saved = userEntityService.save(newUser, avatar, UserRole.ADMIN);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.convertUserEntityToUserDto(saved));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDto.getEmailOrUsername(),
                                loginDto.getPassword()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        UserEntity user = (UserEntity) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertUserToJwtUserResponse(user, jwt));

    }


    @GetMapping("/me")
    public UserDto getMe(@AuthenticationPrincipal UserEntity user) {
        return userDtoConverter.convertUserEntityToUserDto(user);
    }


    private JwtUserResponse convertUserToJwtUserResponse(UserEntity user, String jwt) {
        return JwtUserResponse.builder()
                .fullName(user.getFullName())
                .username(user.getUsername())
                .latLng(user.getLatlong())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .role(user.getRole().name())
                .token(jwt)
                .build();
    }

}
