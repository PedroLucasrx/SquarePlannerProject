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

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

    login(): void {

        this.authService.login({
            email: this.email,
            senha: this.senha
        }).subscribe({

            next: () => {

                console.log('Login realizado!');

                this.router.navigate(['/']);

            },

            error: (erro) => {

                console.error(
                    'Erro ao fazer login:',
                    erro
                );

            }

        });

    }

    irParaCadastro(): void {
        this.router.navigate(['/cadastro']);
    }

}