package com.example.squarePlanner.config;

import com.example.squarePlanner.security.JwtAuthenticationFilter;
import com.example.squarePlanner.service.JwtService;
import com.example.squarePlanner.service.UsuarioDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;


import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
public class SecurityConfig {
    private final JwtService jwtService;
    private final UsuarioDetailsService usuarioDetailsService;

    public SecurityConfig(JwtService jwtService, UsuarioDetailsService usuarioDetailsService){
        this.jwtService = jwtService;
        this.usuarioDetailsService = usuarioDetailsService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        JwtAuthenticationFilter jwtFilter =
                new JwtAuthenticationFilter(
                        jwtService,
                        usuarioDetailsService
                );

        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> {})

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // AUTENTICAÇÃO
                        // =========================

                        .requestMatchers("/auth/**", "/health").permitAll()
                       


                        // =========================
                        // LEITURA
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/provas/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/tarefas/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/ads/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/eventos/**"
                        ).authenticated()


                        // =========================
                        // PROVAS - ADMIN
                        // =========================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/provas/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/provas/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/provas/**"
                        ).hasRole("ADMIN")


                        // =========================
                        // TAREFAS - ADMIN
                        // =========================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/tarefas/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/tarefas/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/tarefas/**"
                        ).hasRole("ADMIN")


                        // =========================
                        // ADS
                        // =========================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/ads/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/ads/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/ads/**"
                        ).hasRole("ADMIN")


                        // =========================
                        // EVENTOS - ADMIN
                        // =========================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/eventos/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/eventos/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/eventos/**"
                        ).hasRole("ADMIN")


                        // =========================
                        // TUDO MAIS
                        // =========================

                        .anyRequest().authenticated()

                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:4200",
                        "https://squareplannerproject.pedrolucasrxsantoss.workers.dev"
                )
        );

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}
