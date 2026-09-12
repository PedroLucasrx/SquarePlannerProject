package com.example.squarePlanner.service;

import com.example.squarePlanner.dtos.evento.CriarEventoDTO;
import com.example.squarePlanner.dtos.evento.EditarEventoDTO;
import com.example.squarePlanner.enity.Evento;
import com.example.squarePlanner.enity.Usuario;
import com.example.squarePlanner.exception.DadosInvalidosException;
import com.example.squarePlanner.exception.EventoNotFound;
import com.example.squarePlanner.exception.JaExisteException;
import com.example.squarePlanner.exception.UsuarioNotFound;
import com.example.squarePlanner.repository.EventoRepository;
import com.example.squarePlanner.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoService {

    EventoRepository eventoRepository;
    UsuarioRepository usuarioRepository;

    public EventoService(
            EventoRepository eventoRepository,
            UsuarioRepository usuarioRepository
    ){
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void criarEvento(CriarEventoDTO dados){
        Evento evento = new Evento(
                dados.nome(),
                dados.data(),
                dados.turma()
        );
        if(dados.nome() == null || dados.nome().isBlank()){
            throw new DadosInvalidosException("Nome do Evento é necessario");
        }

        if(eventoRepository.existsByNomeAndData(dados.nome(),dados.data())){
            throw new JaExisteException("Evento com o mesmo nome ja exite para essa data");
        }
        eventoRepository.save(evento);
    }

    public Evento lerEvento(Long id){
        return eventoRepository.findById(id).orElseThrow(() -> new EventoNotFound("Evento não encontrado"));
    }

    public List<Evento> listarEventos() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsuarioNotFound("Usuário não encontrado")
                );

        if (usuario.getTurma() == null) {
            throw new DadosInvalidosException(
                    "Usuário não possui uma turma definida"
            );
        }

        Long turmaId = usuario.getTurma().getId();

        return eventoRepository.findByTurmaIdOrderByData(turmaId);
    }

    public void deletarEvento(Long id){
        if(!eventoRepository.existsById(id)) throw new EventoNotFound("Evento não encontrado");

        eventoRepository.deleteById(id);
    }

    public void editarEvento(Long id, EditarEventoDTO dados){
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EventoNotFound("Evento não encontrado")
        );
        evento.setNome(dados.nome());
        evento.setData(dados.data());

        eventoRepository.save(evento);
    }
}
