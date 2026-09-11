import {
  AfterViewInit,
  Component,
  ElementRef,
  ViewChild
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';

declare const google: any;

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements AfterViewInit {

  @ViewChild('googleButton') googleButton!: ElementRef;

  email = '';
  senha = '';

  carregando = false;
  mensagemErro = '';

  ngAfterViewInit(): void {

    const verificarGoogle = setInterval(() => {

      if (typeof google !== 'undefined') {

        clearInterval(verificarGoogle);

        google.accounts.id.initialize({
          client_id: '916862102985-p4iiksq618g3tohfpk9c9pgi028tnkop.apps.googleusercontent.com',
          callback: (response: any) => {
            this.carregando = true;
            this.mensagemErro = '';

            this.authService.loginGoogle(response.credential).subscribe({

              next: () => {

              console.log('Login com Google realizado!');

              this.carregando = false;

              this.router.navigate(['/']);

              },

              error: (erro) => {

                console.error('Erro ao fazer login com Google:', erro);

                this.carregando = false;

                this.mensagemErro = 'Não foi possível entrar com o Google. Tente novamente.';

              }

            });
          }
        });

        google.accounts.id.renderButton(
          this.googleButton.nativeElement,
          {
            theme: 'outline',
            size: 'large',
            width: 350,
            text: 'continue_with'
          }
        );

      }

    }, 100);

  }

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