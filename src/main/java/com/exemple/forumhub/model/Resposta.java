package com.exemple.forumhub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A mensagem não pode estar em branco")
    @Column(nullable = false)
    private String mensagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id", nullable = false)
    @NotNull(message = "O tópico não pode ser nulo")
    private com.forum.model.Topico topico;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    @NotNull(message = "O autor não pode ser nulo")
    private com.forum.model.Usuario autor;

    @Column(nullable = false)
    private Boolean solucao = false;
}
