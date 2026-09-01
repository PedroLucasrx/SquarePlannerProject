package com.example.squarePlanner.controller;

import com.example.squarePlanner.dtos.RestResponseDTO;
import com.example.squarePlanner.dtos.atividades.EditarEstadoAtividadeDTO;
import com.example.squarePlanner.service.AtividadesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tarefas/atividades")
public class AtividadesController {

    public final AtividadesService atividadesService;
    public AtividadesController(AtividadesService atividadesService){
        this.atividadesService = atividadesService;
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<RestResponseDTO> editarEstado(
            @PathVariable Long id,
            @RequestBody EditarEstadoAtividadeDTO dado
            ){
        atividadesService.editarEstadoAtividade(id,dado);

        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("estado da atividade editado"));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponseDTO> deletarAtividade(@PathVariable Long id){
        atividadesService.deletarAtividae(id);
        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("atividade deletada"));

    }



}
