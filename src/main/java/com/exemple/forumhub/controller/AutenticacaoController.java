package com.forum.controller;

import com.forum.dto.DadosAutenticacao;
import com.forum.security.DadosTokenJWT;
import com.forum.model.Usuario;
import com.forum.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<DadosTokenJWT> login(@RequestBody @Valid DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.getEmail(), dados.getSenha());
        var authentication = authenticationManager.authenticate(authenticationToken);

        var token = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(token));
    }
}
