package com.exemple.forumhub.service;

import com.exemple.forumhub.model.Topico;
import com.exemple.forumhub.model.Usuario;
import com.exemple.forumhub.repository.CursoRepository;
import com.exemple.forumhub.repository.TopicoRepository;
import com.exemple.forumhub.repository.UsuarioRepository;
import com.exemple.forumhub.security.TokenService;
import com.forum.dto.DadosCadastroTopico;
import com.forum.dto.DadosDetalhamentoTopico;
import com.forum.exception.UsuarioSemPermissaoException;
import com.forum.exception.ValidacaoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;

    public TopicoService(TopicoRepository topicoRepository,
                         CursoRepository cursoRepository,
                         UsuarioRepository usuarioRepository,
                         TokenService tokenService) {
        this.topicoRepository = topicoRepository;
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public DadosDetalhamentoTopico salvar(DadosCadastroTopico dados, String token) {
        validarDados(dados);

        Usuario autor = pegarAutorPeloToken(token);
        com.forum.model.Curso curso = cursoRepository.getReferenceById(dados.getCurso_id());
        Topico topico = new Topico(dados.getTitulo(), dados.getMensagem(), curso, autor);

        topico = topicoRepository.save(topico);

        return new DadosDetalhamentoTopico(topico);
    }

    @Transactional(readOnly = true)
    public DadosDetalhamentoTopico buscar(Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tópico não encontrado"));

        return new DadosDetalhamentoTopico(topico);
    }

    @Transactional(readOnly = true)
    public List<DadosDetalhamentoTopico> listar() {
        List<Topico> topicos = topicoRepository.findAllByOrderByDataCriacaoDesc();
        return topicos.stream().map(DadosDetalhamentoTopico::new).toList();
    }

    @Transactional
    public DadosDetalhamentoTopico atualizar(Long id, DadosCadastroTopico dados, String token) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tópico não encontrado"));

        Usuario autor = pegarAutorPeloToken(token);
        validarPermissaoUsuario(autor, topico);

        validarDados(dados);

        topico.setTitulo(dados.getTitulo());
        topico.setMensagem(dados.getMensagem());
        topico.setCurso(cursoRepository.getReferenceById(dados.getCurso_id()));

        return new DadosDetalhamentoTopico(topico);
    }

    @Transactional
    public void remover(Long id, String token) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tópico não encontrado"));

        Usuario autor = pegarAutorPeloToken(token);
        validarPermissaoUsuario(autor, topico);

        topicoRepository.deleteById(id);
    }

    private void validarDados(DadosCadastroTopico dados) {
        if (!cursoRepository.existsById(dados.getCurso_id())) {
            throw new ValidacaoException("Curso não encontrado");
        }

        if (topicoRepository.existsByTitulo(dados.getTitulo()) || topicoRepository.existsByMensagem(dados.getMensagem())) {
            throw new ValidacaoException("Já existe um tópico com esse título ou mensagem");
        }
    }

    private Usuario pegarAutorPeloToken(String token) {
        String email = tokenService.getSubject(token.replace("Bearer ", "").trim());
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioSemPermissaoException("Usuário não encontrado ou não autenticado"));
    }

    private void validarPermissaoUsuario(Usuario autor, Topico topico) {
        if (!autor.equals(topico.getAutor())) {
            throw new UsuarioSemPermissaoException("Usuário sem permissão para realizar esta operação");
        }
    }
}
