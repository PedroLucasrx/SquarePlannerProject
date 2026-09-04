package com.example.squarePlanner.service;

import com.example.squarePlanner.dtos.ad.*;
import com.example.squarePlanner.enity.Ad;
import com.example.squarePlanner.enity.ProgressoAd;
import com.example.squarePlanner.enity.ProgressoAtividades;
import com.example.squarePlanner.enity.Usuario;
import com.example.squarePlanner.exception.*;
import com.example.squarePlanner.repository.AdRepository;
import com.example.squarePlanner.repository.ProgressoAdRepository;
import com.example.squarePlanner.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdService {

    private final AdRepository adRepository;
    private final ProgressoAdRepository progressoAdRepository;
    private final UsuarioRepository usuarioRepository;

    public AdService(
        AdRepository adRepository,
        ProgressoAdRepository progressoAdRepository,
        UsuarioRepository usuarioRepository
    ){
        this.adRepository = adRepository;
        this.progressoAdRepository = progressoAdRepository;
        this.usuarioRepository = usuarioRepository;
    }


    public void criarAd(CriarAdDTO dados){
        Ad ad = new Ad(
            dados.materia(),
            dados.data(),
            dados.trimestre(),
            dados.proposta()
        );
        if(dados.trimestre() <= 0 || dados.trimestre() >3){
            throw new FormatoInvalidoException("trimestre invalido");
        }

        if(dados.materia() == null || dados.materia().isBlank()){
            throw new DadosInvalidosException("Matéria é obrigatoria");
        }

        adRepository.save(ad);
    }

    public void editarAd(Long id,EditarAdDTO dados){
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFound("Ad não encontrada")
                );
        ad.setMateria(dados.materia());
        ad.setData(dados.data());
        ad.setTrimestre(dados.trimestre());
        ad.setProposta(dados.proposta());

        adRepository.save(ad);
    }

    public Ad lerAd(Long id){
        return adRepository.findById(id).orElseThrow(() -> new AdNotFound("Ad não encontrada"));
    }
    //ANTIGO
    /*public AdsResponseDTO listarAds() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNotFound(""));

        Long usuarioId = usuario.getId();

        List<AdResponseDTO> ads = adRepository.findAll()
                .stream()
                .map(ad -> {

                    Optional<ProgressoAd> progresso =
                            progressoAdRepository.findByUsuarioIdAndAdId(
                                    usuarioId,
                                    ad.getId()
                            );

                    boolean concluido =
                            progresso
                                    .map(ProgressoAd::isConcluido)
                                    .orElse(false);

                    return new AdResponseDTO(
                            ad.getId(),
                            ad.getMateria(),
                            ad.getData(),
                            ad.getTrimestre(),
                            ad.getProposta(),
                            concluido
                    );

                })
                .toList();

        int totalAds = ads.size();

        int adsConcluidas = (int) ads.stream()
                .filter(AdResponseDTO::concluido)
                .count();

        double progresso = totalAds == 0
                ? 0
                : (double) adsConcluidas / totalAds * 100;

        return new AdsResponseDTO(
                ads,
                adsConcluidas,
                totalAds,
                progresso
        );
    }*/
    //NOVO
    public AdsResponseDTO listarAds() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNotFound("Usuario não encontrado"));
        Long usuarioId = usuario.getId();

        // Busca todas as ADs de uma vez
        List<Ad> ads = adRepository.findAll();

        // Busca todos os progressos de uma vez
        List<ProgressoAd> progressos = progressoAdRepository.findByUsuarioId(usuarioId);

        var progressoPorAd = progressos.stream().collect(Collectors.toMap(
                progresso -> progresso.getAd().getId(),
                ProgressoAd::isConcluido
        ));

        List<AdResponseDTO> adsResponse = ads.stream().map(
                ad -> {
                    boolean concuido = progressoPorAd.getOrDefault(
                            ad.getId(),
                            false
                    );
                    return  new AdResponseDTO(
                            ad.getId(),
                            ad.getMateria(),
                            ad.getData(),
                            ad.getTrimestre(),
                            ad.getProposta(),
                            concuido
                    );
                }
        ).toList();

        int totalAds = ads.size();

        int adsConcluidas = (int) adsResponse.stream()
                .filter(AdResponseDTO::concluido)
                .count();

        double progresso = totalAds == 0
                ? 0
                : (double) adsConcluidas / totalAds * 100;



        return new AdsResponseDTO(
                adsResponse,
                adsConcluidas,
                totalAds,
                progresso
        );


    }

    public void deletarAd(Long id){

        if (!adRepository.existsById(id)) {
            throw new AdNotFound("Conteúdo não encontrado");
        }
        adRepository.deleteById(id);
    }

    public void alterarEstado(Long id,EditarEstadoAdDTO estado){
        Ad ad = adRepository.findById(id).orElseThrow(() -> new AdNotFound(""));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsuarioNotFound(""));
        ProgressoAd progresso = progressoAdRepository.findByUsuarioIdAndAdId(
            usuario.getId(),
            ad.getId()
        ).orElseGet( () ->{
            ProgressoAd novo = new ProgressoAd();
            novo.setUsuario(usuario);
            novo.setAd(ad);
            return  novo;
        });
        progresso.setConcluido(estado.concluido());


        progressoAdRepository.save(progresso);
    }

}
