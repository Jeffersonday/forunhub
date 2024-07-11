package com.forum.controller;

import com.forum.dto.DadosCadastroTopico;
import com.forum.service.TopicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    private final TopicoService topicoService;

    @Autowired
    public TopicoController(TopicoService topicoService) {
        this.topicoService = topicoService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosCadastroTopico> salvar(@RequestHeader("Authorization") String token,
                                                      @RequestBody @Valid DadosCadastroTopico dados,
                                                      UriComponentsBuilder uriBuilder) {

        var dadosDetalhamentoTopico = topicoService.salvar(dados, token);
        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(dadosDetalhamentoTopico.getId()).toUri();

        return ResponseEntity.created(uri).body(dadosDetalhamentoTopico);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosCadastroTopico> buscar(@PathVariable Long id) {
        var dadosDetalhamentoTopico = topicoService.buscar(id);
        return ResponseEntity.ok(dadosDetalhamentoTopico);
    }

    @GetMapping
    public ResponseEntity<List<DadosCadastroTopico>> listar() {
        var dadosDetalhamentoTopicos = topicoService.listar();
        return ResponseEntity.ok(dadosDetalhamentoTopicos);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosCadastroTopico> atualizar(@RequestHeader("Authorization") String token,
                                                         @PathVariable Long id,
                                                         @RequestBody @Valid DadosCadastroTopico dados) {
        var dadosDetalhamentoTopico = topicoService.atualizar(id, dados, token);
        return ResponseEntity.ok(dadosDetalhamentoTopico);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> remover(@RequestHeader("Authorization") String token,
                                        @PathVariable Long id) {
        topicoService.remover(id, token);
        return ResponseEntity.noContent().build();
    }
}
