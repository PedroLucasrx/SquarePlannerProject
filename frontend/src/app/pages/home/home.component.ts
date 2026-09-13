import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RouterLink, Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';
import { ThemeService } from '../../services/theme.service';

interface Turma {
  id: number;
  nome: string;
  anoEscolar: {
    id: number;
    nome: string;
    ordem: number;
  };
}

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, CommonModule,FormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {

    mostrarPopupTurma = false;
    carregandoUsuario = true;
    carregandoTurma = false;

    turmas: Turma[] = [];
    anoSelecionado: number | null = null;
    turmaSelecionada: number | null = null;

    constructor(
        public authService: AuthService,
        public themeService: ThemeService,
        private router: Router,
        private http: HttpClient
    ) {
        
    }

    sair(): void {

        this.authService.logout();

        this.router.navigate(['/login']);

    }

    ngOnInit(): void {
        this.verificarTurmaUsuario();
    }

    private verificarTurmaUsuario(): void {
        this.carregandoUsuario = true;

        this.authService.carregarDadosUsuario().subscribe({
            next: (usuario) => {
                this.carregandoUsuario = false;
                this.mostrarPopupTurma = usuario.turmaId === null;

                if (usuario.turmaId === null) {
                    this.carregarTurmas();
                }

            },
            error: (erro) => {
                console.error(
                    'Erro ao carregar dados do usuário:',
                    erro
                );
            }
        });
    }

    private carregarTurmas(): void {
        this.http.get<Turma[]>(
            'https://squareplannerproject.onrender.com/turmas'
        ).subscribe({
            next: (turmas) => {
                this.turmas = turmas;
            },
            error: (erro) => {
                console.error('Erro ao carregar turmas:', erro);
            }
        });
    }

    salvarTurma(): void {

        if (this.turmaSelecionada === null) {
            return;
        }
        this.carregandoTurma = true;

        this.http.put(
            'https://squareplannerproject.onrender.com/auth/turma',
            {
                turmaId: this.turmaSelecionada
            }
        ).subscribe({
            next: () => {
                this.verificarTurmaUsuario();
                this.carregandoTurma = false;
            },
            error: (erro) => {

                console.error(
                    'Erro ao salvar turma:',
                    erro
                );
                this.carregandoTurma = false;

            }
        });

    }

    get turmasFiltradas(): Turma[] {
        if (this.anoSelecionado === null) {
            return [];
        }

        return this.turmas.filter(
            turma => turma.anoEscolar.id === this.anoSelecionado
        );
    }

    get anosEscolares(): Turma['anoEscolar'][] {
        const anos = this.turmas.map(turma => turma.anoEscolar);

        return anos.filter(
            (ano, index, self) =>
                index === self.findIndex(outro => outro.id === ano.id)
        );
    }

}
