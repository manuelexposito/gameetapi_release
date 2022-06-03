package com.salesianostriana.gameetapi.security.jwt;


import com.salesianostriana.gameetapi.security.models.user.UserEntity;
import com.salesianostriana.gameetapi.services.interfaces.UserEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Log
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserEntityService userService;
    private final JwtProvider jwtProvider;

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtProvider.TOKEN_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtProvider.TOKEN_PREFIX))
            return bearerToken.substring(JwtProvider.TOKEN_PREFIX.length());

        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String token = this.getJwtFromRequest(request);

        try {

            if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
                UUID userId = jwtProvider.getUserIdFromJwt(token);

                Optional<UserEntity> user = userService.findById(userId);
                if (user.isPresent()) {

                    UserEntity userEntity = user.get();
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userEntity,
                            userEntity.getRole(),
                            userEntity.getAuthorities()
                    );

                    authenticationToken.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

        } catch (Exception exception) {
            log.info("No se ha podido establecer el contexto de seguridad ( " + exception.getMessage() + ")");
        }

        filterChain.doFilter(request, response);

    }

}