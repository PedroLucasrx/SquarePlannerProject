package com.example.squarePlanner.service;

import com.example.squarePlanner.dtos.conteudos.ConteudoResponseDTO;
import com.example.squarePlanner.dtos.conteudos.ConteudoStateDTO;
import com.example.squarePlanner.dtos.conteudos.CriarConteudoDTO;
import com.example.squarePlanner.dtos.conteudos.EditarConteudoDTO;
import com.example.squarePlanner.dtos.provas.CriarProvaDTO;
import com.example.squarePlanner.dtos.provas.EditarProvaDTO;
import com.example.squarePlanner.dtos.provas.ProvaResponseDTO;
import com.example.squarePlanner.enity.Conteudo;
import com.example.squarePlanner.enity.ProgressoConteudo;
import com.example.squarePlanner.enity.Prova;
import com.example.squarePlanner.enity.Usuario;
import com.example.squarePlanner.exception.*;
import com.example.squarePlanner.repository.ConteudoRepository;
import com.example.squarePlanner.repository.ProgressoConteudoRepository;
import com.example.squarePlanner.repository.ProvaRepository;
import com.example.squarePlanner.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProvasService {
    private final ProvaRepository provaRepository;
    private final ConteudoRepository conteudoRepository;
    private final ProgressoConteudoRepository progressoConteudoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProvasService(
            ProvaRepository provaRepository,
            ConteudoRepository conteudoRepository,
            ProgressoConteudoRepository progressoConteudoRepository,
            UsuarioRepository usuarioRepository
    ){
        this.provaRepository = provaRepository;
        this.conteudoRepository = conteudoRepository;
        this.progressoConteudoRepository = progressoConteudoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void criarProva(CriarProvaDTO dados){


        //todo criar uma função para isso
        if(dados.trimestre() > 3 || dados.trimestre() <= 0){
            throw new FormatoInvalidoException(
                "Trimestre Invalido"
            );
        }

        if(dados.materia() == null || dados.materia().isBlank()){
            throw  new DadosInvalidosException("A materia é obrigatoria");
        }


        if(provaRepository.existsByMateriaAndTrimestre(dados.materia(), dados.trimestre())){
            throw new ProvaJaExisteException("");
        }

        Prova prova = new Prova(
                dados.materia(),
                dados.data(),
                dados.trimestre()
        );

        provaRepository.save(prova);

        for(CriarConteudoDTO conteudo: dados.conteudos()){


            Conteudo novoConteudo = new Conteudo(
                    conteudo.nome(),
                    prova.getId()

            );
            conteudoRepository.save(novoConteudo);
        }


    }

    public void criarListaProvas(List<CriarProvaDTO> lista){
        for(CriarProvaDTO prova: lista){
            //todo criar uma função para isso
            if(prova.trimestre() > 3 || prova.trimestre() <= 0){
                throw new FormatoInvalidoException(
                        "Trimestre Invalido"
                );
            }

            if(provaRepository.existsByMateriaAndTrimestre(prova.materia(), prova.trimestre())){
                throw new ProvaJaExisteException("");
            }


            Prova novaProva = new Prova(
                    prova.materia(),
                    prova.data(),
                    prova.trimestre()
            );
            provaRepository.save(novaProva);

            Long provaId = novaProva.getId();

            for (CriarConteudoDTO conteudo : prova.conteudos()) {


                Conteudo novoConteudo = new Conteudo(
                        conteudo.nome(),
                        provaId

                );
                conteudoRepository.save(novoConteudo);
            }


        }
    }

    public void editarProva(Long id, EditarProvaDTO dados){
        Prova prova = provaRepository.findById(id)
                .orElseThrow(() -> new ProvaNotFound("Prova não encontrada"));
        prova.setMateria(dados.materia());
        prova.setData(dados.data());
        prova.setTrimestre(dados.trimestre());
        provaRepository.save(prova);

        List<Conteudo> conteudosAtuais = conteudoRepository.findByProvaId(id);

        Set<Long> idsRecebidos = dados.conteudos()
                .stream()
                .filter( conteudo->  conteudo.id() == null)
                .map(EditarConteudoDTO::id)
                .collect(Collectors.toSet());

        for (Conteudo conteudo: conteudosAtuais){
            if (!idsRecebidos.contains(conteudo.getId())) {
                conteudoRepository.delete(conteudo);
            }
        }

        for(EditarConteudoDTO conteudo: dados.conteudos()){
            if(conteudoRepository.existsByNome(conteudo.nome())){
                return;
            }

            Conteudo novoConteudo = new Conteudo(
                    conteudo.nome(),
                    prova.getId()
            );
            conteudoRepository.save(novoConteudo);
        }


    }
    public void deletarProva(Long id){
        if(!provaRepository.existsById(id)){
            throw new ProvaNotFound("Prova não encontrada");
        }

        provaRepository.deleteById(id);
    }

    //ANTIGO
    /*public List<ProvaResponseDTO> listarProvas() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado")
                );

        Long usuarioId = usuario.getId();

        return provaRepository.findAll()
                .stream()
                .map(prova -> {

                    List<ConteudoResponseDTO> conteudos =
                            conteudoRepository
                                    .findByProvaId(prova.getId())
                                    .stream()
                                    .map(conteudo -> {

                                        Optional<ProgressoConteudo> progresso =
                                                progressoConteudoRepository
                                                        .findByUsuarioIdAndConteudoId(
                                                                usuarioId,
                                                                conteudo.getId()
                                                        );

                                        boolean concluido =
                                                progresso
                                                        .map(ProgressoConteudo::isConcluido)
                                                        .orElse(false);

                                        return new ConteudoResponseDTO(
                                                conteudo.getId(),
                                                conteudo.getNome(),
                                                concluido
                                        );
                                    })
                                    .toList();

                    int total = conteudos.size();

                    int concluidas = (int) conteudos.stream()
                            .filter(ConteudoResponseDTO::concluido)
                            .count();

                    double progresso = total == 0
                            ? 0
                            : (double) concluidas / total * 100;

                    return new ProvaResponseDTO(
                            prova.getId(),
                            prova.getMateria(),
                            prova.getData(),
                            prova.getTrimestre(),
                            conteudos,
                            concluidas,
                            total,
                            progresso
                    );

                })
                .toList();
    }*/
    //NOVO
    public List<ProvaResponseDTO> listarProvas() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado")
                );

        Long usuarioId = usuario.getId();

        // Busca todas as provas de uma vez
        List<Prova> provas = provaRepository.findAll();

        // Busca todos os conteúdos de uma vez
        List<Conteudo> todosConteudos = conteudoRepository.findAll();

        // Busca todos os progressos desse usuário de uma vez
        List<ProgressoConteudo> progressos =
                progressoConteudoRepository.findByUsuarioId(usuarioId);

        // conteudoId -> concluido
        var progressoPorConteudo = progressos.stream()
                .collect(Collectors.toMap(
                        progresso -> progresso.getConteudo().getId(),
                        ProgressoConteudo::isConcluido
                ));

        // provaId -> lista de conteúdos
        var conteudosPorProva = todosConteudos.stream()
                .collect(Collectors.groupingBy(
                        Conteudo::getProvaId
                ));

        return provas.stream()
                .map(prova -> {

                    List<ConteudoResponseDTO> conteudos =
                            conteudosPorProva
                                    .getOrDefault(
                                            prova.getId(),
                                            List.of()
                                    )
                                    .stream()
                                    .map(conteudo -> {

                                        boolean concluido =
                                                progressoPorConteudo.getOrDefault(
                                                        conteudo.getId(),
                                                        false
                                                );

                                        return new ConteudoResponseDTO(
                                                conteudo.getId(),
                                                conteudo.getNome(),
                                                concluido
                                        );
                                    })
                                    .toList();

                    int total = conteudos.size();

                    int concluidas = (int) conteudos.stream()
                            .filter(ConteudoResponseDTO::concluido)
                            .count();

                    double progresso = total == 0
                            ? 0
                            : (double) concluidas / total * 100;

                    return new ProvaResponseDTO(
                            prova.getId(),
                            prova.getMateria(),
                            prova.getData(),
                            prova.getTrimestre(),
                            conteudos,
                            concluidas,
                            total,
                            progresso
                    );
                })
                .toList();
    }

    public ProvaResponseDTO provaById(Long id){//todo passa o Progresso conteudo pra ca
        Prova prova = provaRepository.findById(id)
                .orElseThrow(() ->
                        new ProvaNotFound("Prova não encontrada")
                );
        List<ConteudoResponseDTO> conteudos =
                conteudoRepository
                        .findByProvaId(id)
                        .stream()
                        .map(conteudo -> new ConteudoResponseDTO(
                                conteudo.getId(),
                                conteudo.getNome(),
                                false
                        ))
                        .toList();

        int total = conteudos.size();

        int concluidas = (int) conteudos.stream()
                .filter(ConteudoResponseDTO::concluido)
                .count();

        double progresso = total == 0
                ? 0
                : (double) concluidas / total * 100;

        return new ProvaResponseDTO(
                prova.getId(),
                prova.getMateria(),
                prova.getData(),
                prova.getTrimestre(),
                conteudos,
                concluidas,
                total,
                progresso
        );
    }

    //ANTIGO
    /*public void alterarEstado(Long id, ConteudoStateDTO estado) {

        Conteudo conteudo = conteudoRepository.findById(id)
                .orElseThrow(() ->
                        new ConteudoNotFound("Conteúdo não encontrado")
                );

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsuarioNotFound("Usuário não encontrado")
                );

        ProgressoConteudo progresso =
                progressoConteudoRepository
                        .findByUsuarioIdAndConteudoId(
                                usuario.getId(),
                                conteudo.getId()
                        )
                        .orElseGet(() -> {

                            ProgressoConteudo novo =
                                    new ProgressoConteudo();

                            novo.setUsuario(usuario);
                            novo.setConteudo(conteudo);

                            return novo;
                        });

        progresso.setConcluido(estado.concluido());

        progressoConteudoRepository.save(progresso);
    }*/
    //NOVO
    public void alterarEstado(Long id, ConteudoStateDTO estado) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        int alterado = progressoConteudoRepository.alterarEstado(
                email,
                id,
                estado.concluido()
        );

        if (alterado == 0) {
            throw new ConteudoNotFound("Conteúdo não encontrado");
        }
    }

    public void deletarConteudo(Long id){
        if (!conteudoRepository.existsById(id)) {
            throw new ConteudoNotFound("Conteúdo não encontrado");
        }

        conteudoRepository.deleteById(id);
    }
}
