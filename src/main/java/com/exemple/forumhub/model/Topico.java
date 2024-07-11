package com.exemple.forumhub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título não pode estar em branco")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "A mensagem não pode estar em branco")
    @Column(nullable = false)
    private String mensagem;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTopico status = StatusTopico.NAO_RESOLVIDO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    @NotNull(message = "O curso não pode ser nulo")
    private com.forum.model.Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    @NotNull(message = "O autor não pode ser nulo")
    private com.forum.model.Usuario autor;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<com.forum.model.Resposta> respostas;

    public Topico(String titulo, String mensagem, com.forum.model.Curso curso, com.forum.model.Usuario autor) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.dataCriacao = LocalDateTime.now();
        this.status = StatusTopico.NAO_RESOLVIDO;
        this.curso = curso;
        this.autor = autor;
    }
}
