package com.example.squarePlanner.service;

import com.example.squarePlanner.dtos.usuario.*;
import com.example.squarePlanner.enity.Turma;
import com.example.squarePlanner.enity.Usuario;
import com.example.squarePlanner.exception.DadosInvalidosException;
import com.example.squarePlanner.exception.JaExisteException;
import com.example.squarePlanner.exception.UsuarioNotFound;
import com.example.squarePlanner.repository.TurmaRepository;
import com.example.squarePlanner.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final GoogleTokenService googleTokenService;
    private final TurmaRepository turmaRepository;


    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            GoogleTokenService googleTokenService,
            TurmaRepository turmaRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.googleTokenService = googleTokenService;
        this.turmaRepository = turmaRepository;
    }

    public void criarUsuario(CriarUsuarioDTO dados) {

        if (usuarioRepository.existsByEmail(dados.email())) {
            throw new JaExisteException("Este email já está cadastrado");
        }
        Turma turma = turmaRepository.findById(dados.turmaId())
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        String senhaCriptografada =
                passwordEncoder.encode(dados.senha());

        Usuario usuario = new Usuario(
                dados.nome(),
                dados.email(),
                senhaCriptografada
        );

        usuario.setTurma(turma);
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

    public LoginGoogleResponseDTO loginGoogle(GoogleLoginDTO dados) {

        GoogleIdToken.Payload payload =
                googleTokenService.validarToken(dados.credential());

        String googleId = payload.getSubject();
        String email = payload.getEmail();
        String nome = (String) payload.get("name");

        Usuario usuario = usuarioRepository
                .findByGoogleId(googleId)
                .orElse(null);

        if (usuario == null) {
            usuario = usuarioRepository
                    .findByEmail(email)
                    .orElse(null);
        }

        // Usuário Google novo: entra autenticado e escolhe a turma na Home.
        if (usuario == null) {
            usuario = new Usuario(
                    nome,
                    email,
                    passwordEncoder.encode(java.util.UUID.randomUUID().toString())
            );
            usuario.setGoogleId(googleId);

            usuarioRepository.save(usuario);
        }

        // Usuário existente que ainda não tinha Google vinculado
        else if (usuario.getGoogleId() == null) {
            usuario.setGoogleId(googleId);
            usuarioRepository.save(usuario);
        }

        String token = jwtService.gerarToken(
                usuario.getEmail(),
                usuario.getNome(),
                usuario.getRole()
        );

        return new LoginGoogleResponseDTO(
                token,
                false
        );
    }

    public void atualizarTurma(EditarTurmaDTO dados) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsuarioNotFound("Usuário não encontrado"));

        Turma turma = turmaRepository.findById(dados.turmaId())
                .orElseThrow(() ->
                        new DadosInvalidosException("Turma não encontrada"));

        usuario.setTurma(turma);

        usuarioRepository.save(usuario);
    }

    public UsuarioLogadoDTO usuarioLogado() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsuarioNotFound("Usuário não encontrado"));

        Long turmaId = usuario.getTurma() != null
                ? usuario.getTurma().getId()
                : null;

        return new UsuarioLogadoDTO(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                turmaId
        );
    }


}



