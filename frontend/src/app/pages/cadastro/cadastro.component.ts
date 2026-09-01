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

  private apiUrl = 'http://localhost:8081/auth';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  cadastrar(): void {

    this.mensagemErro = '';
    this.mensagemSucesso = '';

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

    this.http.post(
      `${this.apiUrl}/cadastro`,
      dados
    ).subscribe({

      next: () => {

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

        if (erro.error?.mensagem) {

          this.mensagemErro =
            erro.error.mensagem;

        } else {

          this.mensagemErro =
            'Não foi possível realizar o cadastro.';
        }

      }

    });

  }

  voltarParaLogin(): void {
    this.router.navigate(['/login']);
  }

}

