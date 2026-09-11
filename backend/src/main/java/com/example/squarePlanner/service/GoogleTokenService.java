package com.example.squarePlanner.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleTokenService {

    private final String clientId;

    public GoogleTokenService(
            @Value("${google.client-id}") String clientId
    ) {
        this.clientId = clientId;
    }

    public GoogleIdToken.Payload validarToken(String credential) {

        try {

            GoogleIdTokenVerifier verifier =
                    new GoogleIdTokenVerifier.Builder(
                            new NetHttpTransport(),
                            GsonFactory.getDefaultInstance()
                    )
                            .setAudience(Collections.singletonList(clientId))
                            .build();

            GoogleIdToken idToken = verifier.verify(credential);

            if (idToken == null) {
                throw new RuntimeException("Token do Google inválido");
            }

            return idToken.getPayload();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Não foi possível validar o token do Google",
                    e
            );
        }
    }
}
