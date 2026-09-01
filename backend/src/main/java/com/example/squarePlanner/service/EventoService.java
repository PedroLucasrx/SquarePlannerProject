package com.example.squarePlanner.service;

import com.example.squarePlanner.dtos.evento.CriarEventoDTO;
import com.example.squarePlanner.dtos.evento.EditarEventoDTO;
import com.example.squarePlanner.enity.Evento;
import com.example.squarePlanner.exception.DadosInvalidosException;
import com.example.squarePlanner.exception.EventoNotFound;
import com.example.squarePlanner.exception.JaExisteException;
import com.example.squarePlanner.repository.EventoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoService {

    EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository){
        this.eventoRepository = eventoRepository;
    }

    public void criarEvento(CriarEventoDTO dados){
        Evento evento = new Evento(
                dados.nome(),
                dados.data()
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

    public List<Evento> listarEventos(){
        return eventoRepository.findAll();
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
