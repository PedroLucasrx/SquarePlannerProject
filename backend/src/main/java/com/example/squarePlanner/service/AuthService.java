package com.example.squarePlanner.service;

import com.example.squarePlanner.dtos.usuario.CriarUsuarioDTO;
import com.example.squarePlanner.dtos.usuario.LoginDTO;
import com.example.squarePlanner.enity.Usuario;
import com.example.squarePlanner.exception.JaExisteException;
import com.example.squarePlanner.exception.UsuarioNotFound;
import com.example.squarePlanner.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void criarUsuario(CriarUsuarioDTO dados) {

        if (usuarioRepository.existsByEmail(dados.email())) {
            throw new JaExisteException("Este email já está cadastrado");
        }

        String senhaCriptografada =
                passwordEncoder.encode(dados.senha());

        Usuario usuario = new Usuario(
                dados.nome(),
                dados.email(),
                senhaCriptografada
        );

        usuarioRepository.save(usuario);
    }

    public String login(LoginDTO dados) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dados.email(),
                        dados.senha()
                )
        );

        Usuario usuario =
                usuarioRepository.findByEmail(dados.email())
                        .orElseThrow(() ->
                                new UsuarioNotFound("")
                        );

        return jwtService.gerarToken(
                usuario.getEmail(),
                usuario.getNome(),
                usuario.getRole()
        );
    }
}




