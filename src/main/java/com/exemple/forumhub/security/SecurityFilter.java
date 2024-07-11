package com.exemple.forumhub.security;

import com.exemple.forumhub.model.Usuario;
import com.exemple.forumhub.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final com.forum.security.TokenService tokenService;
    private final UsuarioRepository repository;

    public SecurityFilter(com.forum.security.TokenService tokenService, UsuarioRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            String subject = tokenService.getSubject(tokenJWT);
            Usuario usuario = repository.findByEmail(subject);

            if (usuario != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        usuario, null, usuario.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "").trim();
        }
        return null;
    }
}
