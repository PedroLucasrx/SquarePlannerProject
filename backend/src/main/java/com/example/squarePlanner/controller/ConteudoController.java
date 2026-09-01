package com.example.squarePlanner.controller;

import com.example.squarePlanner.dtos.conteudos.ConteudoStateDTO;
import com.example.squarePlanner.dtos.RestResponseDTO;
import com.example.squarePlanner.service.ConteudoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/provas/conteudos")
public class ConteudoController {
    private final ConteudoService conteudoService;

    public ConteudoController(ConteudoService conteudoService){
        this.conteudoService = conteudoService;
    }


    @PutMapping("/{id}/estado")
    public ResponseEntity<RestResponseDTO> editarEstado(
            @PathVariable Long id,
            @RequestBody ConteudoStateDTO estado
        ){
        conteudoService.alterarEstado(id,estado);
        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("Estado editado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponseDTO> deletarConteudo(
            @PathVariable Long id
    ){
        conteudoService.deletarConteudo(id);

        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("Conteudo deletado"));
    }

}
