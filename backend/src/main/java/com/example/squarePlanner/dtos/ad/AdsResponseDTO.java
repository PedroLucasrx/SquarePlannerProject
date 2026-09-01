package com.example.squarePlanner.dtos.ad;

import java.util.List;

public record AdsResponseDTO(
        List<AdResponseDTO> ads,
        int adsConcluidas,
        int totalAds,
        double progresso
) {}