import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import {  CriarAd } from '../../services/ad.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AdService } from '../../services/ad.service';
import { Ad } from '../../models/ad';
import { AuthService } from '../../services/auth.service';


@Component({
  selector: 'app-ad',
  standalone: true,
  imports: [RouterLink, FormsModule, CommonModule],
  templateUrl: './ad.component.html',
  styleUrl: './ad.component.scss'
})

export class AdComponent {
  
  ads: Ad[] = [];
  adsConcluidas = 0;
  totalAds = 0;
  progresso = 0;

  trimestreSelecionado: number = 0;

  
  novaAd: CriarAd = {
    materia: '',
    data: '',
    trimestre: 1,
    proposta: ''
  };

  adSelecionado?: Ad;

  mostrarFormularioEdicao = false;

  novaAdEditada: CriarAd = {
    materia: '',
    data: '',
    trimestre: 1,
    proposta: ''
  };
  

  mostrarFormulario = false;

  constructor(
    private AdService: AdService,
    public authService: AuthService
  ){}

  voltarParaHome() {
    window.location.href = '/';
  }

  ngOnInit(): void {
    this.carregarAds();
  }

  carregarAds(): void {
    this.AdService.listarAds().subscribe(dados => {
      this.ads = dados.ads;
      this.adsConcluidas = dados.adsConcluidas;
      this.totalAds = dados.totalAds;
      this.progresso = dados.progresso;
    });

  }

  abrirFormulario(): void {
    this.mostrarFormulario = true;
  }

  fecharFormulario(): void {
    this.mostrarFormulario = false;
  }

  criarAd(): void {
    this.AdService.criarAd(this.novaAd).subscribe({
      next: () => {
        console.log('Prova criada!');

        this.mostrarFormulario = false;

        this.novaAd = {
          materia: '',
          data: '',
          trimestre: 1,
          proposta: ''
        };

        this.carregarAds();
      },

      error: (erro) => {
        console.error('Erro ao criar atividade diversificada:', erro); 
      }

    });

  }

  abrirFormularioEdicao(ad: Ad): void {

    this.novaAdEditada = {
      materia: ad.materia,
      data: ad.data,
      trimestre: ad.trimestre,
      proposta: ad.proposta
    };

    this.adSelecionado = ad;

    this.mostrarFormularioEdicao = true;
  }
  
  fecharFormularioEdicao(): void {
    this.mostrarFormularioEdicao = false;
    this.adSelecionado = undefined;
  }

  salvarEdicao(): void {

    if (!this.adSelecionado) {
      return;
    }

    this.AdService.editarAd(
      this.adSelecionado.id,
      this.novaAdEditada
    ).subscribe({

      next: () => {

        console.log('AD editado!');

        this.mostrarFormularioEdicao = false;

        this.carregarAds();

      },

      error: (erro) => {

        console.error(
          'Erro ao editar AD:',
          erro
        );

      }

    });
  }

  deletarAd(id: number): void {

    const confirmar = confirm(  'Tem certeza que deseja excluir esta AD?');

    if (!confirmar) return;
      
    this.AdService.deletarAd(id).subscribe({

      next: () => {
        console.log('AD deletada!');
        this.carregarAds();

      },

      error: (erro) => {
        console.error('Erro ao deletar AD:',erro);

      }

    });

  }

  alterarEstado(adId: number, concluido: boolean): void {

    this.AdService.editarEstadoAd(
      adId,
      concluido
    ).subscribe({

      next: () => {
        console.log('Estado da ad atualizado!');
        this.carregarAds();
      },

      error: (erro) => {
        console.error(
          'Erro ao alterar estado da ad:',
          erro
        );

      }

    });

  }

  getProgressoTotal(): number {

    const adsFiltradas = this.getAdsFiltradas();

    if (adsFiltradas.length === 0) {
        return 0;
    }

    const concluidas = adsFiltradas.filter(
        ad => ad.concluido
    ).length;

    return (concluidas / adsFiltradas.length) * 100;
  }

  getAdsFiltradas() {

  if (this.trimestreSelecionado === 0) {
    return this.ads;
  }

  return this.ads.filter(
    ad => ad.trimestre == this.trimestreSelecionado
  );
  }

  

}
