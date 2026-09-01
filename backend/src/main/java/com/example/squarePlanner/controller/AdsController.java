package com.example.squarePlanner.controller;

import com.example.squarePlanner.dtos.RestResponseDTO;
import com.example.squarePlanner.dtos.ad.*;
import com.example.squarePlanner.enity.Ad;
import com.example.squarePlanner.service.AdService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ads")
public class AdsController {

    private final AdService adService;

    public AdsController(AdService adService){
        this.adService = adService;
    }


    @PostMapping()
    public ResponseEntity<RestResponseDTO> criarAd(@RequestBody CriarAdDTO dados){
        adService.criarAd(dados);
        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("ad criada"));
    }

    @GetMapping()
    public AdsResponseDTO listarAds(){
        return adService.listarAds();
    }

    @GetMapping("/{id}")
    public Ad lerAd(@PathVariable Long id){
        return adService.lerAd(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponseDTO> deletarAd(@PathVariable Long id){
        adService.deletarAd(id);
        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("ad deletada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponseDTO> editarAd(@PathVariable Long id, @RequestBody EditarAdDTO dados){
        adService.editarAd(id,dados);

        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("Ad editada"));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<RestResponseDTO> editarEstadoAd(@PathVariable Long id, @RequestBody EditarEstadoAdDTO dados){
        adService.alterarEstado(id, dados);
        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("Estado da AD editado"));
    }



}
