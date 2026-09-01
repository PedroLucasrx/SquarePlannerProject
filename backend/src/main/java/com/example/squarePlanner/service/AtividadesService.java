package com.example.squarePlanner.service;


import com.example.squarePlanner.dtos.atividades.EditarEstadoAtividadeDTO;
import com.example.squarePlanner.enity.Atividade;
import com.example.squarePlanner.enity.ProgressoAtividades;
import com.example.squarePlanner.enity.Usuario;
import com.example.squarePlanner.exception.*;
import com.example.squarePlanner.repository.AtividadeRepository;
import com.example.squarePlanner.repository.ProgressoAtividadesRepository;
import com.example.squarePlanner.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AtividadesService {
    AtividadeRepository atividadeRepository;
    ProgressoAtividadesRepository progressoAtividadesRepository;
    UsuarioRepository usuarioRepository;

    public AtividadesService(
            AtividadeRepository atividadeRepository,
            ProgressoAtividadesRepository progressoAtividadesRepository,
            UsuarioRepository usuarioRepository){
        this.atividadeRepository = atividadeRepository;
        this.progressoAtividadesRepository = progressoAtividadesRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void editarEstadoAtividade(Long id, EditarEstadoAtividadeDTO dado){
        Atividade atividade = atividadeRepository.findById(id).orElseThrow(() -> new AtividadeNotFound(""));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNotFound(""));


        ProgressoAtividades progressoAtividades = progressoAtividadesRepository.findByUsuarioIdAndAtividadesId(usuario.getId(), atividade.getId())
                .orElseGet( () -> {
                    ProgressoAtividades novo = new ProgressoAtividades();
                    novo.setUsuario(usuario);
                    novo.setAtividades(atividade);
                    return novo;

                });

        progressoAtividades.setConcluido(dado.concluido());

        progressoAtividadesRepository.save(progressoAtividades);
    }

    public void deletarAtividae(Long id){
        if(!atividadeRepository.existsById(id)){
            throw new AtividadeNotFound("Atividade não existe");
        }
        atividadeRepository.deleteById(id);

    }

}
