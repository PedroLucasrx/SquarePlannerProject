package com.example.squarePlanner.service;

import com.example.squarePlanner.dtos.conteudos.ConteudoStateDTO;
import com.example.squarePlanner.enity.Conteudo;
import com.example.squarePlanner.enity.ProgressoConteudo;
import com.example.squarePlanner.enity.Usuario;
import com.example.squarePlanner.exception.ConteudoNotFound;
import com.example.squarePlanner.exception.UsuarioNotFound;
import com.example.squarePlanner.repository.ConteudoRepository;
import com.example.squarePlanner.repository.ProgressoConteudoRepository;
import com.example.squarePlanner.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ConteudoService {

    private final ConteudoRepository conteudoRepository;
    private final ProgressoConteudoRepository progressoRepository;
    private final UsuarioRepository usuarioRepository;

    public ConteudoService(
            ConteudoRepository conteudoRepository,
            ProgressoConteudoRepository progressoConteudoRepository,
            UsuarioRepository usuarioRepository
    ){
        this.conteudoRepository = conteudoRepository;
        this.progressoRepository = progressoConteudoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void alterarEstado(Long id, ConteudoStateDTO estado) {

        Conteudo conteudo = conteudoRepository.findById(id)
                .orElseThrow(() ->
                        new ConteudoNotFound("Conteúdo não encontrado")
                );

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsuarioNotFound("Usuário não encontrado")
                );

        ProgressoConteudo progresso =
                progressoRepository
                        .findByUsuarioIdAndConteudoId(
                                usuario.getId(),
                                conteudo.getId()
                        )
                        .orElseGet(() -> {

                            ProgressoConteudo novo =
                                    new ProgressoConteudo();

                            novo.setUsuario(usuario);
                            novo.setConteudo(conteudo);

                            return novo;
                        });

        progresso.setConcluido(estado.concluido());

        progressoRepository.save(progresso);
    }

    public void deletarConteudo(Long id){
        if (!conteudoRepository.existsById(id)) {
            throw new ConteudoNotFound("Conteúdo não encontrado");
        }

        conteudoRepository.deleteById(id);
    }
}
