import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  email = '';
  senha = '';

  carregando = false;
  mensagemErro = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login(): void {

    // Limpa erro anterior
    this.mensagemErro = '';

    // Evita múltiplos cliques
    if (this.carregando) {
      return;
    }

    this.carregando = true;

    this.authService.login({
      email: this.email,
      senha: this.senha
    }).subscribe({

      next: () => {

        console.log('Login realizado!');

        this.carregando = false;

        this.router.navigate(['/']);

      },

      error: (erro) => {

        console.error(
          'Erro ao fazer login:',
          erro
        );

        this.carregando = false;

        if (erro.status === 401 || erro.status === 403) {

          this.mensagemErro = 'Email ou senha incorretos.';

        } else {

          this.mensagemErro =
            'Não foi possível conectar ao servidor. Tente novamente.';

        }

      }

    });

  }

  irParaCadastro(): void {
    this.router.navigate(['/cadastro']);
  }

}