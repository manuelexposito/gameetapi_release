package com.salesianostriana.gameetapi.controllers;

import com.salesianostriana.gameetapi.models.requests.RequestSession;
import com.salesianostriana.gameetapi.models.requests.dto.RequestDto;
import com.salesianostriana.gameetapi.models.requests.dto.RequestDtoConverter;
import com.salesianostriana.gameetapi.models.sessions.dto.SessionDto;
import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import com.salesianostriana.gameetapi.services.interfaces.RequestService;
import com.salesianostriana.gameetapi.utils.pagination.PaginationLinksUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private final RequestDtoConverter converter;
    private final PaginationLinksUtils paginationLinksUtils;

    @GetMapping
    public ResponseEntity<Page<RequestDto>> fetchMyRequests(@AuthenticationPrincipal UserEntity currentUser, @PageableDefault(size=10, page=0) Pageable pageable, HttpServletRequest request){
        Page<RequestDto> requestDto = requestService.fetchAllRequests(currentUser, pageable).map(converter::convertToDto);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

        return ResponseEntity.ok().header("link",
                        paginationLinksUtils.createLinkHeader(requestDto, uriBuilder))
                .body(requestDto);
    }

    @PostMapping("/session/{id}/invite/{username}")
    public ResponseEntity<RequestDto> sendInvitation(@AuthenticationPrincipal UserEntity currentUser, @PathVariable("id") Long id, @PathVariable String username){

        RequestDto requestDto = converter.convertToDto(requestService.sendSessionInvitation(currentUser, id, username));

        return ResponseEntity.status(HttpStatus.CREATED).body(requestDto);
    }


    @PostMapping("/friend-request/accept/{username}")
    public ResponseEntity<?> acceptFriendship(@AuthenticationPrincipal UserEntity currentUser, @PathVariable("username") String username){

        requestService.acceptFriendship(currentUser.getUsername(), username);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/friend-request/reject/{username}")
    public ResponseEntity<?> rejectFriendship(@AuthenticationPrincipal UserEntity currentUser, @PathVariable("username") String username){

        requestService.rejectFriendship(currentUser.getUsername(), username);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


    @PostMapping("/friend-request/{username}")
    public ResponseEntity<RequestDto> sendFriendshipRequest(@AuthenticationPrincipal UserEntity currentUser, @PathVariable("username") String username){

        RequestDto requestDto = converter.convertToDto(requestService.sendFriendshipRequest(currentUser, username));

        return ResponseEntity.status(HttpStatus.CREATED).body(requestDto);

    }


    @PostMapping("/accept-session/{id}")
    public ResponseEntity<?> acceptInvitation(@AuthenticationPrincipal UserEntity currentUser, @PathVariable("id") Long sessionId){

        requestService.acceptSessionInvitation(sessionId, currentUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED).build();


    }

    @DeleteMapping("/reject-session/{id}")
    public ResponseEntity<?> rejectInvitation(@AuthenticationPrincipal UserEntity currentUser, @PathVariable Long id){
        requestService.rejectSessionInvitation(id, currentUser.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}
