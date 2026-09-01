import { Component } from '@angular/core';
import { CriarTarefa, EditarTarefa, TarefaService } from '../../services/tarefa.service';
import { Tarefa } from '../../models/tarefa';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-tarefas',
  standalone: true,
  imports: [RouterLink, CommonModule, FormsModule],
  templateUrl: './tarefas.component.html',
  styleUrl: './tarefas.component.scss'
})
export class TarefasComponent {
  
  tarefas: Tarefa[] = [];

  tarefa?: Tarefa;

  novaTarefa: CriarTarefa = {
    materia: '',
    data: '',
    trimestre: 1,
    atividades: []
  };

  tarefaSelecionada?: Tarefa

  novaTarefaEditada: EditarTarefa = {
    materia: '',
    data: '',
    trimestre: 1,
    atividades: []
  };

  trimestreSelecionado: number = 0;



  mostrarFormulario = false;
  mostrarFormularioEdicao = false;
  

  constructor(
    private tarefaService: TarefaService,
    public authService: AuthService
  ){}

  ngOnInit(): void {
    this.carregarTarefas();
  }

  carregarTarefas(): void {

  this.tarefaService.listarTarefas().subscribe({

    next: (dados) => {
      this.tarefas = dados;
    },

    error: (erro) => {
      console.error('Erro ao buscar tarefas:', erro);
    }

    });

  }

  voltarParaHome(): void {
    window.location.href = '/';
  }

  abrirFormulario(): void {
    this.mostrarFormulario = true;
  }

  fecharFormulario(): void {
    this.mostrarFormulario = false;
  }

  abrirFormularioEdicao(tarefa: Tarefa): void{
    this.novaTarefaEditada = {
      materia: tarefa.materia,
      data: tarefa.data,
      trimestre: tarefa.trimestre,

      atividades: tarefa.atividades.map(atividade => ({
        id: atividade.id,
        nome: atividade.nome
      }))
    };

    this.tarefaSelecionada = tarefa;

    this.mostrarFormularioEdicao = true;

  }

  fecharFormularioEdicao(): void {
    this.mostrarFormularioEdicao = false;
    this.tarefaSelecionada = undefined;
  }

  salvarEdicao():void {
    if(!this.tarefaSelecionada){
      return
    }


    this.tarefaService.editarTarefa(this.tarefaSelecionada.id,this.novaTarefaEditada)
    .subscribe({
      next: () => {

        console.log('Tarefa editada!');

        this.mostrarFormularioEdicao = false;

        this.carregarTarefas();

      },

      error: (erro) => {

        console.error(
          'Erro ao editar tarefa:',
          erro
        );

      }

    });
  }

  criarTarefa(): void {

    this.tarefaService.criarTarefa(this.novaTarefa).subscribe({

      next: () => {

        console.log('Tarefa criada!');

        this.mostrarFormulario = false;

        this.novaTarefa = {
          materia: '',
          data: '',
          trimestre: 1,
          atividades: []
        };

        this.carregarTarefas();

      },

      error: (erro) => {
        console.error('Erro ao criar tarefa:', erro);

      }

    });

  }

  deletarTarefa(id: number): void {

    const confirmar = confirm('Tem certeza que deseja excluir esta tarefa?');

    if (!confirmar) {
      return;
    }

    this.tarefaService.deletarTarefa(id).subscribe({

      next: () => {
        console.log('Tarefa deletada!');
        this.carregarTarefas();

      },

      error: (erro) => {
        console.error(
          'Erro ao deletar tarefa:',
          erro
        );

      }

    });

  }

  alterarEstado(atividadeId: number, concluido: boolean): void {

    this.tarefaService.editarEstadoAtividade(
      atividadeId,
      concluido
    ).subscribe({

      next: () => {
        console.log('Estado do conteúdo atualizado!');
        this.carregarTarefas();
      },

      error: (erro) => {

        console.error(
          'Erro ao alterar estado do conteúdo:',
          erro
        );

      }

    });

  }

  adicionarAtividadeTarefaEditada(){
    this.novaTarefaEditada.atividades.push({
      nome: ''
    });
  }

  removerAtividadeTarefaEditada(index: number) {
    const atividade = this.novaTarefaEditada.atividades[index];

    // Conteúdo já existe no banco
    if (atividade.id) {

      this.tarefaService.deletarAtividade(atividade.id).subscribe({

        next: () => {

          console.log('Atividade deletada!');

          this.novaTarefaEditada.atividades.splice(index, 1);

        },

        error: (erro) => {

          console.error(
            'Erro ao deletar atividade:',
            erro
          );

        }

      });

    }else {

      // Conteúdo ainda não foi salvo no banco
      this.novaTarefaEditada.atividades.splice(index, 1);

    }
  }

 

  deletarAtividade(id: number): void {

    const confirmar = confirm('Tem certeza que deseja excluir esta atividade?');

    if (!confirmar) {
      return;
    }

    this.tarefaService.deletarAtividade(id).subscribe({

      next: () => {
        console.log('Atividade deletada!');
        this.carregarTarefas();

      },

      error: (erro) => {
        console.error(
          'Erro ao deletar atividade:',
          erro
        );

      }

    });

  }

  removerAtividade(index: number) {
        this.novaTarefa.atividades.splice(index, 1);
    }

  adicionarAtividade() {

    this.novaTarefa.atividades.push({
      nome: ''
    });
    
  }

  getProgressoTotal(): number {

    const tarefasFiltradas = this.getTarefasFiltradas();

    if (tarefasFiltradas.length === 0) {
        return 0;
    }

    const totalAtividades = tarefasFiltradas.reduce(
        (total, tarefa) => total + tarefa.totalAtividades,
        0
    );

    const atividadesConcluidas = tarefasFiltradas.reduce(
        (total, tarefa) => total + tarefa.atividadesConcluidas,
        0
    );

    if (totalAtividades === 0) {
        return 0;
    }

    return (atividadesConcluidas / totalAtividades) * 100;
  }

  getTarefasFiltradas() {
    if (this.trimestreSelecionado === 0) {
      return this.tarefas;
    }

    return this.tarefas.filter(
      tarefa => tarefa.trimestre == this.trimestreSelecionado
    );
  }


}
