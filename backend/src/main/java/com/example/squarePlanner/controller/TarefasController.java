package com.example.squarePlanner.controller;


import com.example.squarePlanner.dtos.RestResponseDTO;
import com.example.squarePlanner.dtos.tarefas.CriarTarefaDTO;
import com.example.squarePlanner.dtos.tarefas.EditarTarefaDTO;
import com.example.squarePlanner.dtos.tarefas.TarefaResponseDTO;
import com.example.squarePlanner.service.TarefaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tarefas")
public class TarefasController {
    TarefaService tarefaService;

    public TarefasController(TarefaService tarefaService){
        this.tarefaService = tarefaService;
    }

    @PostMapping()
    public ResponseEntity<RestResponseDTO> criarTarefa(@RequestBody CriarTarefaDTO dados){
        tarefaService.criarTarefa(dados);

        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("tarefa criada"));
    }

    @GetMapping()
    public List<TarefaResponseDTO> listarTarefas(){
        return tarefaService.listarTarefas();
    }

    @GetMapping("/{id}")
    public TarefaResponseDTO lerTarefa(@PathVariable Long id){
        return tarefaService.lerTarefa(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponseDTO> deletarTarefa(@PathVariable Long id){
        tarefaService.deletarTarefa(id);

        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("tarefa deletada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponseDTO> editarTarefa(
            @PathVariable Long id,
            @RequestBody EditarTarefaDTO dados)
    {
        tarefaService.editarTarefa(id,dados);
        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("tarefa editada"));
    }
}
