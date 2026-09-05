package com.example.squarePlanner.controller;

import com.example.squarePlanner.dtos.RestResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<RestResponseDTO> health() {
        return ResponseEntity.status(HttpStatus.OK).body(new RestResponseDTO("tudo certo"));
    }
}