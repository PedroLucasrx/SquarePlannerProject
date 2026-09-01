package com.example.squarePlanner.service;

import com.example.squarePlanner.dtos.atividades.AtividadesResponseDTO;
import com.example.squarePlanner.dtos.atividades.EditarAtividadeDTO;
import com.example.squarePlanner.dtos.tarefas.CriarTarefaAtividadeDTO;
import com.example.squarePlanner.dtos.tarefas.CriarTarefaDTO;
import com.example.squarePlanner.dtos.tarefas.EditarTarefaDTO;
import com.example.squarePlanner.dtos.tarefas.TarefaResponseDTO;
import com.example.squarePlanner.enity.Atividade;
import com.example.squarePlanner.enity.ProgressoAtividades;
import com.example.squarePlanner.enity.Tarefa;
import com.example.squarePlanner.enity.Usuario;
import com.example.squarePlanner.exception.*;
import com.example.squarePlanner.repository.AtividadeRepository;
import com.example.squarePlanner.repository.ProgressoAtividadesRepository;
import com.example.squarePlanner.repository.TarefasRepository;
import com.example.squarePlanner.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TarefaService {
    TarefasRepository tarefasRepository;
    AtividadeRepository atividadeRepository;
    ProgressoAtividadesRepository progressoAtividadesRepository;
    UsuarioRepository usuarioRepository;

    public TarefaService(
        TarefasRepository tarefasRepository,
        AtividadeRepository atividadeRepository,
        ProgressoAtividadesRepository progressoAtividadesRepository,
        UsuarioRepository usuarioRepository
    ){
        this.tarefasRepository = tarefasRepository;
        this.atividadeRepository = atividadeRepository;
        this.progressoAtividadesRepository = progressoAtividadesRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void criarTarefa(CriarTarefaDTO dados){
        Tarefa tarefa = new Tarefa(
                dados.materia(),
                dados.data(),
                dados.trimestre()
        );
        if(dados.materia() == null || dados.materia().isBlank()){
            throw  new DadosInvalidosException("Nome da materia é necessario");
        }

        if(tarefasRepository.existsByMateriaAndTrimestre(dados.materia(), dados.trimestre())){
            throw new JaExisteException("Tarefa ja exite");
        }
        tarefasRepository.save(tarefa);

        for (CriarTarefaAtividadeDTO atividade: dados.atividades()){
            atividadeRepository.save( new Atividade(
                    atividade.nome(),
                    tarefa.getId()
            ));

        }


    }

    public List<TarefaResponseDTO> listarTarefas() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsuarioNotFound(""));

        return tarefasRepository.findAll()
                .stream()
                .map(tarefa -> {

                    List<AtividadesResponseDTO> atividades =
                            atividadeRepository.findByTarefaId(tarefa.getId())
                                    .stream()
                                    .map(atividade -> {

                                        Optional<ProgressoAtividades> progresso =
                                                progressoAtividadesRepository
                                                        .findByUsuarioIdAndAtividadesId(
                                                                usuario.getId(),
                                                                atividade.getId()
                                                        );

                                        boolean concluido =
                                                progresso
                                                        .map(ProgressoAtividades::isConcluido)
                                                        .orElse(false);

                                        return new AtividadesResponseDTO(
                                                atividade.getId(),
                                                atividade.getNome(),
                                                concluido
                                        );

                                    })
                                    .toList();

                    int total = atividades.size();

                    int concluidas = (int) atividades.stream()
                            .filter(AtividadesResponseDTO::concluido)
                            .count();

                    double progresso = total == 0
                            ? 0
                            : (double) concluidas / total * 100;

                    return new TarefaResponseDTO(
                            tarefa.getId(),
                            tarefa.getMateria(),
                            tarefa.getData(),
                            tarefa.getTrimestre(),
                            atividades,
                            concluidas,
                            total,
                            progresso
                    );

                })
                .toList();
    }

    public TarefaResponseDTO lerTarefa(Long id){//todo passar o progressoAtividades para ca
        Tarefa tarefa = tarefasRepository.findById(id).orElseThrow(() -> new TarefaNotFound(""));
        List<AtividadesResponseDTO> atividades = atividadeRepository.findByTarefaId(id).stream()
                .map(
                        atividade -> new AtividadesResponseDTO(
                                atividade.getId(),
                                atividade.getNome(),
                                false

                        )
                ).toList();

        int total = atividades.size();

        int concluidas = (int) atividades.stream()
                .filter(AtividadesResponseDTO::concluido)
                .count();

        double progresso = total == 0
                ? 0
                : (double) concluidas / total * 100;


        return new TarefaResponseDTO(
                tarefa.getId(),
                tarefa.getMateria(),
                tarefa.getData(),
                tarefa.getTrimestre(),
                atividades,
                concluidas,
                total,
                progresso
        );

    }

    public void deletarTarefa(Long id){
        if (!tarefasRepository.existsById(id)) throw new TarefaNotFound("");

        tarefasRepository.deleteById(id);
    }

    public void editarTarefa(Long id, EditarTarefaDTO dados){
        Tarefa tarefa = tarefasRepository.findById(id).orElseThrow(() -> new TarefaNotFound(""));
        tarefa.setMateria(dados.materia());
        tarefa.setData(dados.data());
        tarefa.setTrimestre(dados.trimestre());
        tarefasRepository.save(tarefa);


        List<Atividade> atividadesAtuais = atividadeRepository.findByTarefaId(id);

        Set<Long> idsRecebidos = dados.atividades()
                .stream()
                .filter(atividade -> atividade.id() != null)
                .map(EditarAtividadeDTO::id)
                .collect(Collectors.toSet());

        for (Atividade atividade : atividadesAtuais) {

            if (!idsRecebidos.contains(atividade.getId())) {

                atividadeRepository.delete(atividade);
            }
        }

        for(EditarAtividadeDTO atividadeEditada: dados.atividades()){
            Atividade atividade;
            if(atividadeEditada.id() == null){
                 atividade = new Atividade(atividadeEditada.nome(), tarefa.getId());
            }else{
                atividade = atividadeRepository.findById(atividadeEditada.id())
                        .orElseThrow(() -> new AtividadeNotFound(""));

                atividade.setNome(atividadeEditada.nome());
            }



            atividadeRepository.save(atividade);

        }
    }



}
