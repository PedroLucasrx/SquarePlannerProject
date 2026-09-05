import { Component } from '@angular/core';
import { CriarEvento, EventoService } from '../../services/evento.service';
import { Evento } from '../../models/evento';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-eventos',
  standalone: true,
  imports: [RouterLink, FormsModule, CommonModule],
  templateUrl: './eventos.component.html',
  styleUrl: './eventos.component.scss'
})
export class EventosComponent {

  // ==========================================
  // EVENTOS
  // ==========================================

  eventos: Evento[] = [];

  evento?: Evento;

  novoEvento = {
    nome: '',
    data: ''
  };

  eventoSelecionado?: Evento;

  mostrarFormulario = false;
  mostrarFormularioEdicao = false;
  carregandoEventos = false;

  novoEventoEditado: CriarEvento = {
    nome: '',
    data: ''
  };


  // ==========================================
  // CALENDÁRIO
  // ==========================================

  dataAtual = new Date();

  mesAtual = this.dataAtual.getMonth();

  anoAtual = this.dataAtual.getFullYear();


  // ==========================================
  // CONSTRUTOR
  // ==========================================

  constructor(
    private EventoService: EventoService,
    public authService: AuthService
  ) {}


  // ==========================================
  // INICIALIZAÇÃO
  // ==========================================

  ngOnInit(): void {
    this.carregarEventos();
  }


  // ==========================================
  // VOLTAR PARA HOME
  // ==========================================

  voltarParaHome() {
    window.location.href = '/';
  }


  // ==========================================
  // CARREGAR EVENTOS
  // ==========================================

  carregarEventos(): void {

    this.carregandoEventos = true;

    this.EventoService.listarEventos().subscribe({

      next: (dados) => {

        this.eventos = dados;
        this.carregandoEventos = false;

      },

      error: (erro) => {

        console.error(
          'Erro ao buscar eventos:',
          erro
        );

        this.carregandoEventos = false;

      }

    });

  }


  // ==========================================
  // ABRIR FORMULÁRIO
  // ==========================================

  abrirFormulario(): void {

    this.mostrarFormulario = true;

  }


  // ==========================================
  // FECHAR FORMULÁRIO
  // ==========================================

  fecharFormulario(): void {

    this.mostrarFormulario = false;

  }


  // ==========================================
  // ABRIR FORMULÁRIO DE EDIÇÃO
  // ==========================================

  abrirFormularioEdicao(evento: Evento): void {

    this.novoEventoEditado = {

      nome: evento.nome,

      data: evento.data

    };

    this.eventoSelecionado = evento;

    this.mostrarFormularioEdicao = true;

  }


  // ==========================================
  // FECHAR FORMULÁRIO DE EDIÇÃO
  // ==========================================

  fecharFormularioEdicao(): void {

    this.mostrarFormularioEdicao = false;

    this.eventoSelecionado = undefined;

  }


  // ==========================================
  // CRIAR EVENTO
  // ==========================================

  criarEvento(): void {

    this.EventoService.criarEvento(
      this.novoEvento
    ).subscribe({

      next: () => {

        console.log('Evento criado!');

        this.mostrarFormulario = false;

        this.novoEvento = {

          nome: '',

          data: ''

        };

        this.carregarEventos();

      },

      error: (erro) => {

        console.error(
          'Erro ao criar evento:',
          erro
        );

      }

    });

  }


  // ==========================================
  // SALVAR EDIÇÃO
  // ==========================================

  salvarEdicao(): void {

    if (!this.eventoSelecionado) {

      return;

    }

    this.EventoService.editarEvento(

      this.eventoSelecionado.id,

      this.novoEventoEditado

    ).subscribe({

      next: () => {

        console.log('Evento editado!');

        this.mostrarFormularioEdicao = false;

        this.carregarEventos();

      },

      error: (erro) => {

        console.error(
          'Erro ao editar evento:',
          erro
        );

      }

    });

  }


  // ==========================================
  // DELETAR EVENTO
  // ==========================================

  deletarEvento(id: number): void {

    const confirmar = confirm(
      'Tem certeza que deseja excluir este evento?'
    );

    if (!confirmar) return;

    this.EventoService.deletarEvento(id)
      .subscribe({

        next: () => {

          console.log(
            'Evento deletado!'
          );

          this.carregarEventos();

        },

        error: (erro) => {

          console.error(
            'Erro ao deletar evento:',
            erro
          );

        }

      });

  }


  // ==========================================
  // CALENDÁRIO
  // ==========================================

  get nomeMesAtual(): string {

    const data = new Date(
      this.anoAtual,
      this.mesAtual,
      1
    );

    return data.toLocaleDateString(
      'pt-BR',
      {
        month: 'long',
        year: 'numeric'
      }
    );

  }


  // ==========================================
  // DIAS DO CALENDÁRIO
  // ==========================================

  get diasCalendario(): Date[] {

    const primeiroDia = new Date(
      this.anoAtual,
      this.mesAtual,
      1
    );

    const ultimoDia = new Date(
      this.anoAtual,
      this.mesAtual + 1,
      0
    );


    /*
     * getDay():
     *
     * 0 = Domingo
     * 1 = Segunda
     * 2 = Terça
     * ...
     * 6 = Sábado
     *
     * Como queremos começar na segunda-feira,
     * fazemos esse ajuste.
     */

    let primeiroDiaSemana =
      primeiroDia.getDay();

    primeiroDiaSemana =
      primeiroDiaSemana === 0
        ? 6
        : primeiroDiaSemana - 1;


    const dias: Date[] = [];


    // ==========================================
    // DIAS DO MÊS ANTERIOR
    // ==========================================

    for (
      let i = primeiroDiaSemana;
      i > 0;
      i--
    ) {

      const data = new Date(
        this.anoAtual,
        this.mesAtual,
        1 - i
      );

      dias.push(data);

    }


    // ==========================================
    // DIAS DO MÊS ATUAL
    // ==========================================

    for (
      let dia = 1;
      dia <= ultimoDia.getDate();
      dia++
    ) {

      dias.push(
        new Date(
          this.anoAtual,
          this.mesAtual,
          dia
        )
      );

    }


    // ==========================================
    // DIAS DO PRÓXIMO MÊS
    // ==========================================

    while (dias.length % 7 !== 0) {

      const ultimo = dias[dias.length - 1];

      const proximo = new Date(ultimo);

      proximo.setDate(
        ultimo.getDate() + 1
      );

      dias.push(proximo);

    }

    return dias;

  }


  // ==========================================
  // VERIFICAR SE O DIA É DO MÊS ATUAL
  // ==========================================

  ehMesAtual(data: Date): boolean {

    return (

      data.getMonth() === this.mesAtual &&

      data.getFullYear() === this.anoAtual

    );

  }


  // ==========================================
  // VERIFICAR SE É HOJE
  // ==========================================

  ehHoje(data: Date): boolean {

    const hoje = new Date();

    return (

      data.getDate() === hoje.getDate() &&

      data.getMonth() === hoje.getMonth() &&

      data.getFullYear() === hoje.getFullYear()

    );

  }


  // ==========================================
  // PEGAR EVENTOS DE UM DIA
  // ==========================================

  getEventosDoDia(data: Date): Evento[] {

    return this.eventos.filter(
      evento => {

        const dataEvento =
          this.converterData(evento.data);

        return (

          dataEvento.getDate() === data.getDate() &&

          dataEvento.getMonth() === data.getMonth() &&

          dataEvento.getFullYear() === data.getFullYear()

        );

      }
    );

  }


  // ==========================================
  // CONVERTER DATA
  // ==========================================

  private converterData(
    dataString: string
  ): Date {

    /*
     * Se a API devolver:
     *
     * 2026-09-15
     *
     * usamos os números diretamente para
     * evitar problemas de fuso horário.
     */

    const partes =
      dataString.split('-');

    if (partes.length === 3) {

      return new Date(

        Number(partes[0]),

        Number(partes[1]) - 1,

        Number(partes[2])

      );

    }

    return new Date(dataString);

  }


  // ==========================================
  // MÊS ANTERIOR
  // ==========================================

  mesAnterior(): void {

    this.mesAtual--;

    if (this.mesAtual < 0) {

      this.mesAtual = 11;

      this.anoAtual--;

    }

  }


  // ==========================================
  // PRÓXIMO MÊS
  // ==========================================

  proximoMes(): void {

    this.mesAtual++;

    if (this.mesAtual > 11) {

      this.mesAtual = 0;

      this.anoAtual++;

    }

  }


  // ==========================================
  // VOLTAR PARA HOJE
  // ==========================================

  irParaHoje(): void {

    const hoje = new Date();

    this.mesAtual = hoje.getMonth();

    this.anoAtual = hoje.getFullYear();

  }


  // ==========================================
  // ABRIR EVENTO
  // ==========================================

  abrirEvento(evento: Evento): void {

    this.abrirFormularioEdicao(evento);

  }

}