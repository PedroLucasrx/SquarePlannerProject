package com.example.squarePlanner.controller;


import com.example.squarePlanner.dtos.conteudos.ConteudoStateDTO;
import com.example.squarePlanner.dtos.provas.CriarProvaDTO;
import com.example.squarePlanner.dtos.provas.EditarProvaDTO;
import com.example.squarePlanner.dtos.provas.ProvaResponseDTO;
import com.example.squarePlanner.dtos.RestResponseDTO;
import com.example.squarePlanner.service.ProvasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/provas")
public class ProvasController {
    ProvasService provasService;

    public ProvasController(ProvasService provasService){
        this.provasService = provasService;
    }

    @PostMapping()
    public ResponseEntity<RestResponseDTO> createProva(@RequestBody CriarProvaDTO dados){
        provasService.criarProva(dados);
        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("Prova criada"));
    }

    @PostMapping("/all")
    public ResponseEntity<RestResponseDTO> createListaProvas(@RequestBody List<CriarProvaDTO> dados){

        provasService.criarListaProvas(dados);
        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("lista criada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponseDTO> editarProva(
            @PathVariable Long id,
            @RequestBody EditarProvaDTO dados
    ){
        provasService.editarProva(id,dados);
        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("Prova editada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponseDTO> deletarProva(
            @PathVariable Long id
    ){
        provasService.deletarProva(id);
        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("Prova deletada"));
    }

    @GetMapping
    public List<ProvaResponseDTO> listarProvas(){
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();



        return provasService.listarProvas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProvaResponseDTO> lerProva(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(
                provasService.provaById(id)
        );
    }

    @PutMapping("/conteudos/{id}/estado")
    public ResponseEntity<RestResponseDTO> editarEstado(
            @PathVariable Long id,
            @RequestBody ConteudoStateDTO estado
    ){
        provasService.alterarEstado(id,estado);
        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("Estado editado"));
    }

    @DeleteMapping("conteudos/{id}")
    public ResponseEntity<RestResponseDTO> deletarConteudo(
            @PathVariable Long id
    ){
        provasService.deletarConteudo(id);

        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("Conteudo deletado"));
    }


}
