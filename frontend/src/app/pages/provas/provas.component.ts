import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EditarProva, CriarProva } from '../../services/prova.service';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';


import { Prova } from '../../models/provas';
import { ProvaService } from '../../services/prova.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-provas',
  standalone: true,
  imports: [FormsModule,RouterLink,CommonModule],
  templateUrl: './provas.component.html',
  styleUrl: './provas.component.scss'
})

export class ProvasComponent implements OnInit {

    provas: Prova[] = [];

    prova ?: Prova

    mostrarFormulario = false;
    mostrarFormularioEdicao = false;
    carregandoProvas = false;
    

    novaProva: CriarProva = {
        materia: '',
        data: '',
        trimestre: 1,
        conteudos: []
    };

    provaSelecionada ?: Prova

    novaProvaEditada: EditarProva = {
        materia: '',
        data: '',
        trimestre: 1,
        conteudos: []
    }

    trimestreSelecionado: number = 0;




    constructor(private provaService: ProvaService,public authService: AuthService) {}

    ngOnInit(): void {
        this.carregarProvas();
    }

    carregarProvas(): void {

        this.carregandoProvas = true;

        this.provaService.listarProvas().subscribe({

            next: (dados) => {
                this.provas = dados;
                this.carregandoProvas = false;
            },

            error: (erro) => {
                console.error('Erro ao buscar provas:', erro);
                this.carregandoProvas = false;
            }

        });

    }

    abrirFormulario(): void {
        this.mostrarFormulario = true;

    }

    fecharFormulario(): void {
        this.mostrarFormulario = false;
    }

    criarProva(): void {

        this.provaService.criarProva(this.novaProva).subscribe({

            next: () => {

                console.log('Prova criada!');

                this.mostrarFormulario = false;

                this.novaProva = {
                    materia: '',
                    data: '',
                    trimestre: 1,
                    conteudos: []
                };

                this.carregarProvas();

            },

            error: (erro) => {
                console.error('Erro ao criar prova:', erro);

            }

        });

    }

    voltarParaHome(): void {
        window.location.href = '/';
    }

    adicionarConteudo() {
        this.novaProva.conteudos.push({
            nome: ''
        });
    }

    adicionarConteudoProvaEditada(){
        this.novaProvaEditada.conteudos.push({
            nome: ''
        });
    }

    removerConteudo(index: number) {
        this.novaProva.conteudos.splice(index, 1);
    }

    removerConteudoEditado(index: number): void {

    const conteudo = this.novaProvaEditada.conteudos[index];

    // Conteúdo já existe no banco
    if (conteudo.id) {

        this.provaService.deletarConteudo(conteudo.id).subscribe({

            next: () => {

                console.log('Conteúdo deletado!');

                this.novaProvaEditada.conteudos.splice(index, 1);

            },

            error: (erro) => {

                console.error(
                    'Erro ao deletar conteúdo:',
                    erro
                );

            }

        });

    } else {

        // Conteúdo ainda não foi salvo no banco
        this.novaProvaEditada.conteudos.splice(index, 1);

    }
}

    deletarConteudo(id: number): void {

        const confirmar = confirm('Tem certeza que deseja excluir esta conteudo?');

        if (!confirmar) {
        return;
        }

        this.provaService.deletarConteudo(id).subscribe({

        next: () => {
            console.log('Atividade deletada!');
            this.carregarProvas();

        },

        error: (erro) => {
            console.error(
            'Erro ao deletar conteudo:',
            erro
            );

        }

        });

    }

    deletarProva(id: number): void {

    const confirmar = confirm(  'Tem certeza que deseja excluir esta Prova?');

    if (!confirmar) return;
      
    this.provaService.deletarProva(id).subscribe({

      next: () => {
        console.log('AD deletada!');
        this.carregarProvas();

      },

      error: (erro) => {
        console.error('Erro ao deletar Provas:',erro);

      }

    });

    }


    alterarEstado(conteudoId: number, concluido: boolean): void {

        this.provaService.editarEstadoConteudo(
            conteudoId,
            concluido
        ).subscribe({

            next: () => {

                console.log('Estado do conteúdo atualizado!');
                this.carregarProvas();

            },

            error: (erro) => {

                console.error(
                    'Erro ao alterar estado do conteúdo:',
                    erro
                );

            }

        });

    }

    abrirFormularioEdicao(prova: Prova): void {
        this.novaProvaEditada = {
            materia: prova.materia,
            data: prova.data,
            trimestre: prova.trimestre,

            conteudos: prova.conteudos.map(conteudo => ({
                id: conteudo.id,
                nome: conteudo.nome
            }))
        };

        this.provaSelecionada = prova;

        this.mostrarFormularioEdicao = true;
    }     

   fecharFormularioEdicao(){
        this.mostrarFormularioEdicao = false
        this.provaSelecionada = undefined
   }

   salvarEdicao():void{
        if(!this.provaSelecionada){
            return
        }

        this.provaService.editarProva(this.provaSelecionada.id,this.novaProvaEditada)
        .subscribe({
            next: () => {

                console.log('Prova editada!');

                this.mostrarFormularioEdicao = false;

                this.carregarProvas();

            },

            error: (erro) => {

                console.error(
                    'Erro ao editar prova:',
                    erro
                );

            }

        });


    }   

    getProgressoTotal(): number {

        const provasFiltradas = this.getProvasFiltradas();

        if (provasFiltradas.length === 0) {
            return 0;
        }

        const totalProvas = provasFiltradas.reduce(
            (total, prova) => total + prova.totalConteudos,
            0
        );

        const conteudosConcluidos = provasFiltradas.reduce(
            (total, prova) => total + prova.conteudosConcluidos,
            0
        );

        if (totalProvas === 0) {
            return 0;
        }

        return (conteudosConcluidos / totalProvas) * 100;
    }

    getProvasFiltradas() {
        if (this.trimestreSelecionado === 0) {
            return this.provas;
        }

        return this.provas.filter(
            prova => prova.trimestre == this.trimestreSelecionado
        );
    }


}