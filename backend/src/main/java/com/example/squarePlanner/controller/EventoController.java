package com.example.squarePlanner.controller;

import com.example.squarePlanner.dtos.RestResponseDTO;
import com.example.squarePlanner.dtos.evento.CriarEventoDTO;
import com.example.squarePlanner.dtos.evento.EditarEventoDTO;
import com.example.squarePlanner.enity.Evento;
import com.example.squarePlanner.service.EventoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventos")
public class EventoController {
    EventoService eventoService;

    public EventoController(EventoService eventoService){
        this.eventoService = eventoService;
    }

    @PostMapping()
    public ResponseEntity<RestResponseDTO> criarEvento(@RequestBody CriarEventoDTO dados){
        eventoService.criarEvento(dados);
        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("evento criado"));
    }

    @GetMapping("/{id}")
    public Evento lerEvento(@PathVariable Long id){
        return eventoService.lerEvento(id);
    }

    @GetMapping()
    public List<Evento> listarEventos(){
        return eventoService.listarEventos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponseDTO> deletarEvento(@PathVariable Long id){
        eventoService.deletarEvento(id);

        return  ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("Evento deletado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponseDTO> editarEvento(
            @PathVariable Long id,
            @RequestBody EditarEventoDTO dados){
        eventoService.editarEvento(id,dados);
        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("Evento editado"));
    }
}
