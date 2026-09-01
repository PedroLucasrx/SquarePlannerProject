package com.example.squarePlanner.controller;

import com.example.squarePlanner.dtos.RestResponseDTO;
import com.example.squarePlanner.dtos.usuario.CriarUsuarioDTO;
import com.example.squarePlanner.dtos.usuario.LoginDTO;
import com.example.squarePlanner.dtos.usuario.LoginResponseDTO;
import com.example.squarePlanner.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<RestResponseDTO> cadastro(
            @RequestBody CriarUsuarioDTO dados
    ) {

        authService.criarUsuario(dados);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RestResponseDTO("Usuário criado com sucesso"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginDTO dados
    ) {

        String token = authService.login(dados);

        return ResponseEntity.ok(
                new LoginResponseDTO(token));
    }
}
