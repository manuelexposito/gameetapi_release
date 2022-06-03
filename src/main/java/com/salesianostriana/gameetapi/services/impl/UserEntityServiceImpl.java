package com.salesianostriana.gameetapi.services.impl;

import com.salesianostriana.gameetapi.errors.exceptions.alreadyexists.UserAlreadyExistsException;
import com.salesianostriana.gameetapi.errors.exceptions.entitynotfound.SingleEntityNotFoundException;
import com.salesianostriana.gameetapi.repositories.UserEntityRepository;
import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import com.salesianostriana.gameetapi.security.models.user.dto.CreateUserDto;
import com.salesianostriana.gameetapi.security.models.user.role.UserRole;
import com.salesianostriana.gameetapi.services.interfaces.StorageService;
import com.salesianostriana.gameetapi.services.interfaces.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.io.*;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import java.io.*;
import java.nio.file.Files;

@Service("userDetailsService")
@RequiredArgsConstructor
public class UserEntityServiceImpl implements UserEntityService, UserDetailsService {


    private final UserEntityRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;
    private final ImageScalerService imageScaler;

    @Override
    public UserEntity save(CreateUserDto newUser, MultipartFile avatar, UserRole role) {

        if (repository.findFirstByEmail(newUser.getEmail()).isEmpty() || repository.findFirstByUsername(newUser.getUsername()).isEmpty()) {
            String uri = null;

            if (avatar != null) {

                String url = storageService.uploadFile(avatar);
                String ext = avatar.getContentType();

                //TODO: Escalar las imagenes
              /*  try {

                    BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(avatar.getBytes()));
                    BufferedImage resized = imageScaler.simpleResizeImage(originalImage, 128);
                    OutputStream out = Files.newOutputStream(new ByteArrayOutputStream(Files.readAllBytes(Paths.get(URI.create()))));
                    MultipartFile avatarResized = ImageIO.write()
                 *//*   BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(avatar.getBytes()));
                    BufferedImage resized = imageScaler.simpleResizeImage(originalImage, 128);
                    OutputStream out = Files.newOutputStream(Paths.get(URI.create(url)));
                    ImageIO.write(resized, ext, out);*//*

                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                uri = url;
            }

            UserEntity user = UserEntity.builder()
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .avatar(uri)
                    .birthdate(newUser.getBirthdate())
                    .username(newUser.getUsername())
                    .fullName(newUser.getFullName())
                    .biography(newUser.getBiography())
                    .email(newUser.getEmail())
                    .latlong(newUser.getLatlng())
                    .interests(newUser.getInterests())
                    .role(role)
                    .build();


            return repository.save(user);

        } else throw new UserAlreadyExistsException();


    }


    @Override
    public UserEntity save(UserEntity userEntity) {

        return repository.save(userEntity);
    }


    @Override
    public UserEntity edit(CreateUserDto edit, MultipartFile avatar, UserEntity currentUser) {
        return null;
    }

    @Override
    public UserEntity findByUsername(String username) {

        Optional<UserEntity> searchUser = repository.findFirstByUsername(username);

        if (searchUser.isPresent()) {

            return searchUser.get();


        } else throw new SingleEntityNotFoundException(UserEntity.class);


    }


    @Override
    public List<UserEntity> saveAll(List<UserEntity> list) {
        return null;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Page<UserEntity> fetchUsers(UserEntity currentUser, Pageable pageable) {
        //TODO: Gestionar mediante alguna consulta que busque segun INTERESES
        return repository.findUsers(pageable);
    }

    @Override
    public List<String> findAvatarsFromSession(List<String> usernameList) {
        return repository.findAvatarFromSession(usernameList);
    }

    @Override
    public Page<UserEntity> getMyFriends(UserEntity currentUser, Pageable pageable) {


        return repository.findMyFriends(currentUser.getId(), pageable);
    }


    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {

        if (input.contains("@")) {
            return this.repository.findFirstByEmail(input)
                    .orElseThrow(() -> new UsernameNotFoundException(input + " no encontrado"));
        }

        return this.repository.findFirstByUsername(input)
                .orElseThrow(() -> new UsernameNotFoundException(input + " no encontrado"));
    }
}
