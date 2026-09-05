import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './cadastro.component.html',
  styleUrl: './cadastro.component.scss'
})
export class CadastroComponent {

  nome = '';
  email = '';
  senha = '';
  confirmarSenha = '';

  mensagemErro = '';
  mensagemSucesso = '';
  carregando = false;

  private apiUrl = 'https://squareplannerproject.onrender.com/auth';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  cadastrar(): void {

    this.mensagemErro = '';
    this.mensagemSucesso = '';

    // Evita múltiplos cliques
    if (this.carregando) {
      return;
    }

    // Verifica se todos os campos foram preenchidos
    if (
      !this.nome ||
      !this.email ||
      !this.senha ||
      !this.confirmarSenha
    ) {

      this.mensagemErro =
        'Preencha todos os campos.';

      return;
    }

    // Verifica se as senhas são iguais
    if (this.senha !== this.confirmarSenha) {

      this.mensagemErro =
        'As senhas não coincidem.';

      return;
    }

    const dados = {
      nome: this.nome,
      email: this.email,
      senha: this.senha
    };

    // Começa o carregamento
    this.carregando = true;

    this.http.post(
      `${this.apiUrl}/cadastro`,
      dados
    ).subscribe({

      next: () => {

        this.carregando = false;

        this.mensagemSucesso =
          'Cadastro realizado com sucesso!';

        // Limpa os campos
        this.nome = '';
        this.email = '';
        this.senha = '';
        this.confirmarSenha = '';

        // Depois de 1 segundo vai para o login
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1000);

      },

      error: (erro) => {

        console.error(
          'Erro ao realizar cadastro:',
          erro
        );

        this.carregando = false;

        if (erro.error?.mensagem) {

          this.mensagemErro =
            erro.error.mensagem;

        } else {

          this.mensagemErro =
            'Não foi possível realizar o cadastro. Tente novamente.';

        }

      }

    });

  }

  voltarParaLogin(): void {
    this.router.navigate(['/login']);
  }

}

